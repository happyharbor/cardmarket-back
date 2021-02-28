package io.happyharbor.cardmarket.core.service;

import io.happyharbor.cardmarket.api.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final StockService stockService;

    @Scheduled(cron = "${schedule.update-prices}")
    public void updatePrices() {
        stockService.updatePrices();
    }
}
