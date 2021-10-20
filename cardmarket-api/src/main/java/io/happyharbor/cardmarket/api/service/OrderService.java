package io.happyharbor.cardmarket.api.service;

import io.happyharbor.cardmarket.api.dto.order.NoCardsInMonthsRequest;
import io.happyharbor.cardmarket.api.dto.order.NoCardsInMonthsResult;

import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<OutputStream> getShippingAddresses();

    CompletableFuture<NoCardsInMonthsResult> getNumberOfCardsIn(NoCardsInMonthsRequest request);
}
