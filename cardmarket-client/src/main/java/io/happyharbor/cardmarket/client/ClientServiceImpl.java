package io.happyharbor.cardmarket.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.happyharbor.cardmarket.api.dto.Account;
import io.happyharbor.cardmarket.api.dto.market.ProductDetailed;
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
    private static final int MAX_STOCK_ARTICLES = 100;
    private static final int STARTING_STOCK_INDEX = 1;
    private static final int MAX_OTHER_USERS_ARTICLES = 1000;
    private static final int STARTING_OTHER_USERS_INDEX = 0;

    private final CardmarketClient client;

    @Override
    public CompletableFuture<Account> getAccountInfo() {
        return client.sendGetRequest("account", new TypeReference<GetAccountResponse>() {})
                     .thenApply(GetAccountResponse::getAccount);
    }

    @Override
    public CompletableFuture<Map<GroupedArticle, BigDecimal>> getUserArticles(final String userId) {
        return CompletableFuture.completedFuture(getUserArticlesRecursive(STARTING_OTHER_USERS_INDEX, userId))
                .thenApply(f -> f.stream()
                                 .filter(a -> "NM".equals(a.getCondition()) || "MT".equals(a.getCondition()))
                                 .filter(a -> a.getPrices().stream().anyMatch(p -> p.getCurrencyId() == 1))
                                 .collect(Collectors.toMap(GroupedArticle::new, article ->
                                         article.getPrices().stream().filter(p -> p.getCurrencyId() == 1).findAny()
                                                .orElseThrow().getArticlePrice(), (o1, o2) -> o1.compareTo(o2) > 0 ? o2 : o1)));
    }

    @Override
    public CompletableFuture<List<MyArticle>> getStock() {
        return CompletableFuture.completedFuture(getStockRecursive(STARTING_STOCK_INDEX));
    }

    @Override
    public CompletableFuture<List<NotUpdatedArticle>> updateArticles(List<MyArticle> articles) {

        if (articles.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        val notUpdatedArticles = new LinkedList<NotUpdatedArticle>();
        var start = 0;
        while (start <= articles.size()) {
            int end = Math.min(start + MAX_STOCK_ARTICLES, articles.size());

            val innerNotUpdatedArticles = client.sendPutRequest("stock",
                    new TypeReference<UpdateArticleResponse>() {},
                    new UpdateArticlesRequestOuter(articles.subList(start, end).stream().map(UpdateArticlesRequest::of).collect(toList())))
                    .join()
                    .getNotUpdatedArticles();

            notUpdatedArticles.addAll(innerNotUpdatedArticles);
            start += MAX_STOCK_ARTICLES;
        }
        notUpdatedArticles.forEach(a -> log.warn("Article: {} was not updated, because: {}", a.getArticle(), a.getError()));
        return CompletableFuture.completedFuture(notUpdatedArticles);
    }

    @Override
    public CompletableFuture<List<Order>> getOrdersBy(final FilteredOrdersRequest request) {
        return client.sendGetRequest(String.format("orders/%s/%s", request.getActor().getName(), request.getState().getName()),
                new TypeReference<GetFilterOrdersResponse>() {})
                .thenApply(GetFilterOrdersResponse::getOrders);
    }

    @Override
    public CompletableFuture<ProductDetailed> getProductsDetails(final Long productId) {
        return client.sendGetRequest("products/" + productId,
                new TypeReference<GetProductResponse>() {})
                .thenApply(GetProductResponse::getProduct);
    }

    private List<OtherUserArticle> getUserArticlesRecursive(final int start, final String userId) {

        val queryMap = new HashMap<String, String>();
        queryMap.put("start", Integer.toString(start));
        queryMap.put("maxResults", Integer.toString(MAX_OTHER_USERS_ARTICLES));

        val articles = client.sendGetRequest(queryMap,
                "users/" + userId + "/articles",
                new TypeReference<GetOtherUserArticleResponse>() {})
                .thenApply(response -> response == null ? new ArrayList<OtherUserArticle>() : response.getArticles())
                .join();

        if (articles.size() == MAX_OTHER_USERS_ARTICLES) {
            articles.addAll(getUserArticlesRecursive(start + MAX_OTHER_USERS_ARTICLES, userId));
        }
        return articles;
    }

    private List<MyArticle> getStockRecursive(final int start) {
        val articles = client.sendGetRequest("stock/" + start, new TypeReference<GetStockResponse>() {})
                .join()
                .getArticles();
        if (articles != null && articles.size() == MAX_STOCK_ARTICLES) {
            articles.addAll(getStockRecursive(start + MAX_STOCK_ARTICLES));
        }
        return articles;
    }
}
