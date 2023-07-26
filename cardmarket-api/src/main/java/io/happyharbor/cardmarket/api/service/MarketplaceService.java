package io.happyharbor.cardmarket.api.service;

import java.util.concurrent.CompletableFuture;

public interface MarketplaceService {
    CompletableFuture<byte[]> getProductList();
}
