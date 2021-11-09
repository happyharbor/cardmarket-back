package io.happyharbor.cardmarket.api.service;

import io.happyharbor.cardmarket.api.dto.Account;
import io.happyharbor.cardmarket.api.dto.MyArticle;
import io.happyharbor.cardmarket.api.dto.NotUpdatedArticle;
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
}
