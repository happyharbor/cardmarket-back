package io.happyharbor.cardmarket.api.service;

import io.happyharbor.cardmarket.api.dto.account.Account;
import io.happyharbor.cardmarket.api.dto.market.*;
import io.happyharbor.cardmarket.api.dto.order.FilteredOrdersRequest;
import io.happyharbor.cardmarket.api.dto.order.Order;
import io.happyharbor.cardmarket.api.dto.stock.*;
import io.happyharbor.cardmarket.api.helper.SimilarArticle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ClientService {
    CompletableFuture<Account> getAccountInfo();

    CompletableFuture<Map<SimilarArticle, BigDecimal>> getUserArticles(final String userId);

    CompletableFuture<List<MyArticle>> getStock();

    CompletableFuture<List<Order>> getOrdersBy(final FilteredOrdersRequest request);

    CompletableFuture<List<Order>> getOrdersByMax1YearBack(final FilteredOrdersRequest request);

    CompletableFuture<ProductDetailed> getProductsDetails(Long productId);

    CompletableFuture<String> getProductList();

    CompletableFuture<List<MyArticle>> increaseStockQuantity(final List<ChangeStockQuantityArticle> changeStockQuantityArticles);

    CompletableFuture<Void> decreaseStockQuantity(final List<ChangeStockQuantityArticle> changeStockQuantityArticles);

    CompletableFuture<List<NotUpdatedArticle>> updateArticles(List<MyArticle> otherUserArticles);

    CompletableFuture<List<NotAddedArticle>> addArticles(final List<MyArticle> articles);

    CompletableFuture<List<DeletedArticle>> deleteArticles(final List<MyArticle> articles);

    CompletableFuture<Boolean> goOnVacation(final boolean onVacation);


    /////////////////
    /* Marketplace */
    /////////////////

    CompletableFuture<GetGamesResponse> getGames();
    CompletableFuture<GetExpansionsResponse> getExpansionsBy(GetExpansionsRequest request);
}
