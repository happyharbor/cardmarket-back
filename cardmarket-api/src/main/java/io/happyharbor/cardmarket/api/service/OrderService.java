package io.happyharbor.cardmarket.api.service;

import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<OutputStream> getShippingAddresses();
}
