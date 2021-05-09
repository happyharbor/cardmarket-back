package io.happyharbor.cardmarket.api.service;

import io.happyharbor.cardmarket.api.dto.Account;
import io.happyharbor.cardmarket.api.dto.market.ProductDetailed;
import io.happyharbor.cardmarket.api.dto.order.FilteredOrdersRequest;
import io.happyharbor.cardmarket.api.dto.order.Order;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import io.happyharbor.cardmarket.api.dto.stock.NotUpdatedArticle;
import io.happyharbor.cardmarket.api.helper.GroupedArticle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ClientService {
    CompletableFuture<Account> getAccountInfo();

    CompletableFuture<Map<GroupedArticle, BigDecimal>> getUserArticles(final String userId);

    CompletableFuture<List<MyArticle>> getStock();

    CompletableFuture<List<NotUpdatedArticle>> updateArticles(List<MyArticle> otherUserArticles);

    CompletableFuture<List<Order>> getOrdersBy(final FilteredOrdersRequest request);

    CompletableFuture<ProductDetailed> getProductsDetails(Long productId);
}
