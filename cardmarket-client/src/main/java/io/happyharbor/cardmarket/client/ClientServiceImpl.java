package io.happyharbor.cardmarket.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.spotify.futures.CompletableFutures;
import io.happyharbor.cardmarket.api.dto.Account;
import io.happyharbor.cardmarket.api.dto.order.FilteredOrdersRequest;
import io.happyharbor.cardmarket.api.dto.order.Order;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import io.happyharbor.cardmarket.api.dto.stock.NotUpdatedArticle;
import io.happyharbor.cardmarket.api.dto.stock.OtherUserArticle;
import io.happyharbor.cardmarket.api.helper.GroupedArticle;
import io.happyharbor.cardmarket.api.lib.FutureCollectors;
import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.client.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final CardmarketClient client;

    @Override
    public CompletableFuture<Account> getAccountInfo() {
        return client.sendGetRequest("account", new TypeReference<GetAccountResponse>() {})
                     .thenApply(GetAccountResponse::getAccount);
    }

    @Override
    public CompletableFuture<Map<GroupedArticle, BigDecimal>> getUserArticles(final String userId) {

        return getUserArticlesRecursive(0, 10, 1000, userId)
                .thenApply(f -> f.stream().flatMap(List::stream).collect(toList()))
                .thenApply(f -> f.stream()
                                 .filter(a -> "NM".equals(a.getCondition()) || "MT".equals(a.getCondition()))
                                 .filter(a -> a.getPrices().stream().anyMatch(p -> p.getCurrencyId() == 1))
                                 .collect(Collectors.toMap(GroupedArticle::new, article ->
                                         article.getPrices().stream().filter(p -> p.getCurrencyId() == 1).findAny()
                                                .orElseThrow().getArticlePrice(), (o1, o2) -> o1.compareTo(o2) > 0 ? o2 : o1)));
    }

    @Override
    public CompletableFuture<List<MyArticle>> getStock() {

        return getStockRecursive(1, 10, 100)
                .thenApply(f -> f.stream().flatMap(List::stream).collect(toList()));
    }

    @Override
    public CompletableFuture<List<NotUpdatedArticle>> updateArticles(List<MyArticle> articles) {

        if (articles.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        List<List<MyArticle>> listArticles = new ArrayList<>();
        int start = 0;
        final int max = 100;
        while (start < articles.size()) {
            int end = Math.min(start + max, articles.size());
            listArticles.add(articles.subList(start, end));
            start += max;
        }
        return listArticles.stream()
                .parallel()
                .map(as -> client.sendPutRequest("stock",
                        new TypeReference<UpdateArticleResponse>() {},
                        new UpdateArticlesRequestOuter(as.stream().map(UpdateArticlesRequest::of).collect(toList()))))
                .map(f -> f.thenApply(UpdateArticleResponse::getNotUpdatedArticles))
                .collect(CompletableFutures.joinList())
                .thenApply(f -> f.stream().flatMap(List::stream).collect(toList()))
                .thenApply(notUpdatedArticles -> {
                    notUpdatedArticles.forEach(a -> log.warn("Article: {} was not updated, because: {}", a.getArticle(), a.getError()));
                    return notUpdatedArticles;
        });
    }

    @Override
    public CompletableFuture<List<Order>> getOrdersBy(final FilteredOrdersRequest request) {
        return client.sendGetRequest(String.format("orders/%s/%s", request.getActor().getName(), request.getState().getName()),
                new TypeReference<GetFilterOrdersResponse>() {})
                .thenApply(GetFilterOrdersResponse::getOrders);
    }


    private CompletableFuture<List<List<OtherUserArticle>>> getUserArticlesRecursive(
            final int start, final int maxTasks, int maxResults, final String userId) {

        final List<OtherUserArticle> fakeArticles = getFakeArticles(maxResults);

        return IntStream.range(0, maxTasks)
                .boxed()
                .map(i -> {
                    val queryMap = new HashMap<String, String>();
                    queryMap.put("idUser", userId);
                    queryMap.put("start", Integer.toString(start + i * maxResults));
                    queryMap.put("maxResults", Integer.toString(maxResults));
                    return client.sendGetRequest(queryMap,
                            "users/" + queryMap.get("idUser") + "/articles",
                            new TypeReference<GetOtherUserArticleResponse>() {
                            })
                            .thenApply(GetOtherUserArticleResponse::getArticles);
                })
                .collect(FutureCollectors.sequenceNoFailCollector(t -> {
                    log.debug("Get user articles for user {}, failed due to {}", userId, t.getMessage());
                    return fakeArticles;
                }))
                .thenCompose(f -> {
            if (f.stream().noneMatch(l -> l == null || l.isEmpty() || l.size() < maxResults)) {
                return getUserArticlesRecursive(start + maxResults, maxTasks, maxResults, userId)
                        .thenApply(newArticles -> {
                            newArticles.addAll(f);
                            return newArticles;
                        });
            }
            return CompletableFuture.completedFuture(f);
        });
    }

    private CompletableFuture<List<List<MyArticle>>> getStockRecursive(final int start, final int maxTasks, int maxResults) {
        return IntStream.range(0, maxTasks)
                .boxed()
                .map(i -> client.sendGetRequest("stock/" + (start + i * maxResults), new TypeReference<GetStockResponse>() {})
                    .thenApply(GetStockResponse::getArticles))
                .collect(CompletableFutures.joinList())
                .thenCompose(f -> {
                    if (f.stream().noneMatch(l -> l == null || l.isEmpty() || l.size() < maxResults)) {
                        final int nextStart = start + maxResults * maxTasks;
                        return getStockRecursive(nextStart, maxTasks, maxResults)
                                .thenApply(newArticles -> {
                                    newArticles.addAll(f);
                                    return newArticles;
                                });
                    }
                    return CompletableFuture.completedFuture(f);
                });
    }

    private static List<OtherUserArticle> getFakeArticles(final int maxResults) {
        return IntStream.range(0, maxResults)
                .boxed()
                .map(i -> OtherUserArticle.builder().build())
                .collect(toList());
    }
}
