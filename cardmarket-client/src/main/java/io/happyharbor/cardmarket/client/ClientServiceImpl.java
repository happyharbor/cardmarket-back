package io.happyharbor.cardmarket.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.happyharbor.cardmarket.api.dto.account.Account;
import io.happyharbor.cardmarket.api.dto.market.ProductDetailed;
import io.happyharbor.cardmarket.api.dto.order.FilteredOrdersRequest;
import io.happyharbor.cardmarket.api.dto.order.Order;
import io.happyharbor.cardmarket.api.dto.stock.*;
import io.happyharbor.cardmarket.api.helper.SimilarArticle;
import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.client.dto.*;
import io.happyharbor.cardmarket.client.dto.account.GetAccountResponse;
import io.happyharbor.cardmarket.client.dto.account.GetVacationResponse;
import io.happyharbor.cardmarket.client.dto.stock.*;
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
    public static final String STOCK_ENDPOINT = "stock";

    private final CardmarketClient client;

    @Override
    public CompletableFuture<Account> getAccountInfo() {
        return client.sendGetRequest("account", new TypeReference<GetAccountResponse>() {})
                     .thenApply(GetAccountResponse::getAccount);
    }

    @Override
    public CompletableFuture<Map<SimilarArticle, BigDecimal>> getUserArticles(final String userId) {
        return CompletableFuture.completedFuture(getUserArticlesRecursive(STARTING_OTHER_USERS_INDEX, userId))
                .thenApply(f -> f.stream()
                                 .filter(a -> "NM".equals(a.getCondition()) || "MT".equals(a.getCondition()))
                                 .filter(a -> a.getPrices().stream().anyMatch(p -> p.getCurrencyId() == 1))
                                 .collect(Collectors.toMap(SimilarArticle::new, article ->
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

            val innerNotUpdatedArticles = client.sendPutRequest(STOCK_ENDPOINT,
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
    public CompletableFuture<List<NotAddedArticle>> addArticles(final List<MyArticle> articles) {

        if (articles.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        val notAddedArticles = new LinkedList<InsertedArticle>();
        var start = 0;
        while (start <= articles.size()) {
            int end = Math.min(start + MAX_STOCK_ARTICLES, articles.size());

            val innerNotAddedArticles = client.sendPostRequest(STOCK_ENDPOINT,
                    new TypeReference<AddArticleResponse>() {},
                    new AddArticlesRequestOuter(articles.subList(start, end).stream().map(AddArticlesRequest::of).collect(toList())))
                    .join()
                    .getInsertedArticles()
                    .stream()
                    .filter(insertedArticle -> !insertedArticle.isSuccess())
                    .collect(toList());

            notAddedArticles.addAll(innerNotAddedArticles);
            start += MAX_STOCK_ARTICLES;
        }
        notAddedArticles.forEach(a -> log.warn("Article: {} was not added, because: {}", a.getNotAddedArticle(), a.getError()));
        return CompletableFuture.completedFuture(notAddedArticles.stream().map(InsertedArticle::getNotAddedArticle).collect(toList()));
    }

    @Override
    public CompletableFuture<List<DeletedArticle>> deleteArticles(final List<MyArticle> articles) {

        if (articles.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        val notDeletedArticles = new LinkedList<DeletedArticle>();
        var start = 0;
        while (start <= articles.size()) {
            int end = Math.min(start + MAX_STOCK_ARTICLES, articles.size());

            val innerNotDeletedArticles = client.sendDeleteRequest(STOCK_ENDPOINT,
                    new TypeReference<DeleteArticleResponse>() {},
                    new DeleteArticlesRequestOuter(articles.subList(start, end).stream().map(DeleteArticlesRequest::of).collect(toList())))
                    .join()
                    .getArticles()
                    .stream()
                    .filter(deletedArticle -> !deletedArticle.isSuccess())
                    .collect(toList());

            notDeletedArticles.addAll(innerNotDeletedArticles);
            start += MAX_STOCK_ARTICLES;
        }
        notDeletedArticles.forEach(a -> log.warn("Article: {} was not deleted", a));
        return CompletableFuture.completedFuture(notDeletedArticles);
    }

    @Override
    public CompletableFuture<Boolean> goOnVacation(final boolean onVacation) {
        val queryMap = new HashMap<String, String>();
        queryMap.put("onVacation", Boolean.toString(onVacation));
        return client.sendPutRequest(queryMap, "account/vacation", new TypeReference<GetVacationResponse>() {}, null)
                .thenApply(r -> onVacation ? r.getMessage().equals("Successfully set the account on vacation.") :
                                             r.getMessage().equals("Successfully returned the account from vacation."));
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

    @Override
    public CompletableFuture<List<MyArticle>> increaseStockQuantity(final List<ChangeStockQuantityArticle> changeStockQuantityArticles) {
        return CompletableFuture.completedFuture(client.sendPutRequest("stock/increase",
                new TypeReference<IncreaseQuantityArticleResponse>() {},
                new ChangeQuantityArticlesRequestOuter(changeStockQuantityArticles))
                .join()
                .getNotChangedArticles());
    }

    @Override
    public CompletableFuture<Void> decreaseStockQuantity(final List<ChangeStockQuantityArticle> changeStockQuantityArticles) {
        client.sendPutRequest("stock/decrease",
                new TypeReference<DecreaseQuantityArticleResponse>() {},
                new ChangeQuantityArticlesRequestOuter(changeStockQuantityArticles))
                .join();
        return CompletableFuture.completedFuture(null);
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
