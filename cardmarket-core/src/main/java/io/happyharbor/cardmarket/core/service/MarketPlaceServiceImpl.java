package io.happyharbor.cardmarket.core.service;

import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.api.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class MarketPlaceServiceImpl implements MarketplaceService {

    private final ClientService clientService;

    @Override
    public CompletableFuture<byte[]> getProductList() {
        return clientService.getProductList()
                .thenApply(s -> {
                    if (StringUtils.isBlank(s)) {
                        throw new IllegalArgumentException("content is either null or blank");
                    }

                    return Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8));
                });
    }
}
