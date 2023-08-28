package io.happyharbor.cardmarket.api.service;

import io.happyharbor.cardmarket.api.dto.market.GetExpansionsRequest;
import io.happyharbor.cardmarket.api.dto.market.GetExpansionsResponse;
import io.happyharbor.cardmarket.api.dto.market.GetGamesResponse;

import java.util.concurrent.CompletableFuture;

public interface MarketplaceService {
    CompletableFuture<byte[]> getProductList();
    CompletableFuture<GetGamesResponse> getGames();
    CompletableFuture<GetExpansionsResponse> getExpansions(GetExpansionsRequest request);
}
