package io.happyharbor.cardmarket.core.service;

import io.happyharbor.cardmarket.api.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    @Override
    public void updatePrices() {
        log.debug("Update prices starting...");
        log.info("Update prices finished");
    }
}
