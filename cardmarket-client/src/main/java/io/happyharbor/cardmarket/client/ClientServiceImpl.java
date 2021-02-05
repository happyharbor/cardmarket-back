package io.happyharbor.cardmarket.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.happyharbor.cardmarket.api.dto.Account;
import io.happyharbor.cardmarket.api.dto.order.FilteredOrdersRequest;
import io.happyharbor.cardmarket.api.dto.order.Order;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import io.happyharbor.cardmarket.api.dto.stock.NotUpdatedArticle;
import io.happyharbor.cardmarket.api.dto.stock.OtherUserArticle;
import io.happyharbor.cardmarket.api.helper.GroupedArticle;
import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.client.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final CardmarketClient client;
    private final JavaCompletableFuturesSequencer sequencer;

    @Override
    public CompletableFuture<Account> getAccountInfo() {
        return client.sendGetRequest("account", new TypeReference<GetAccountResponse>() {})
                     .thenApply(GetAccountResponse::getAccount);
    }

    @Override
    public CompletableFuture<Map<GroupedArticle, BigDecimal>> getUserArticles(final String userId) {

        return createTasksOtherUsers(0, 10, 1000, new LinkedList<>(), userId)
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

        return createTasks(1, 10, 100, new LinkedList<>()).getRight()
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
        List<CompletableFuture<List<NotUpdatedArticle>>> stock = listArticles.stream()
                .parallel()
                .map(as -> client.sendPutRequest("stock",
                        new TypeReference<UpdateArticleResponse>() {},
                        new UpdateArticlesRequestOuter(as.stream().map(UpdateArticlesRequest::of).collect(toList()))))
                .map(f -> f.thenApply(UpdateArticleResponse::getNotUpdatedArticles))
                .collect(toList());

        return sequencer.sequence(stock)
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

    private CompletableFuture<List<List<OtherUserArticle>>> createTasksOtherUsers(
            final int s, final int maxTasks, int maxResults, List<List<OtherUserArticle>> articles, final String userId) {

        final List<CompletableFuture<List<OtherUserArticle>>> futures = new ArrayList<>();
        int start = s;
        for (int i = 0; i < maxTasks; i++) {
            val queryMap = new HashMap<String, String>();
            queryMap.put("idUser", userId);
            queryMap.put("start", Integer.toString(start));
            queryMap.put("maxResults", Integer.toString(maxResults));
            CompletableFuture<List<OtherUserArticle>> userArticlesFuture = client.sendGetRequest(queryMap,
                    "users/" + queryMap.get("idUser") + "/articles",
                    new TypeReference<GetOtherUserArticleResponse>() {})
                .thenApply(GetOtherUserArticleResponse::getArticles);
            futures.add(userArticlesFuture);
            start += maxResults;
        }

        CompletableFuture<List<List<OtherUserArticle>>> sequence = sequencer.sequence(futures);

        int finalStart = start;
        return sequence.thenCompose(f -> {
            articles.addAll(f);
            if (f.stream().noneMatch(l -> l == null || l.isEmpty() || l.size() < maxResults)) {
                return createTasksOtherUsers(finalStart, maxTasks, maxResults, articles, userId);
            }
            return CompletableFuture.completedFuture(articles);
        });
    }

    private Pair<Integer, CompletableFuture<List<List<MyArticle>>>> createTasks(final int s, final int maxTasks,
                                                                                int maxResults, List<List<MyArticle>> articles) {
        final List<CompletableFuture<List<MyArticle>>> futures = new ArrayList<>();
        int start = s;
        for (int i = 0; i < maxTasks; i++) {
            CompletableFuture<List<MyArticle>> userArticlesFuture = client.sendGetRequest(
                    Collections.emptyMap(),
                    "stock/" + start,
                    new TypeReference<GetStockResponse>() {})
                .thenApply(GetStockResponse::getArticles);
            futures.add(userArticlesFuture);
            start += maxResults;
        }

        CompletableFuture<List<List<MyArticle>>> sequence = sequencer.sequence(futures);

        int finalStart = start;
        CompletableFuture<List<List<MyArticle>>> listCompletableFuture = sequence.thenCompose(f -> {
            articles.addAll(f);
            if (f.stream().noneMatch(l -> l == null || l.isEmpty() || l.size() < maxResults)) {
                return createTasks(finalStart, maxTasks, maxResults, articles).getRight();
            }
            return CompletableFuture.completedFuture(articles);
        });


        return MutablePair.of(start, listCompletableFuture);
    }
}
