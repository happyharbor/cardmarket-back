package io.happyharbor.cardmarket.rest.controller;

import io.happyharbor.cardmarket.api.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    @PutMapping("/update-prices")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updatePrices() {
        stockService.updatePrices();
    }

    @PutMapping("/update-prices-from-csv")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updatePricesFromCsv() {
        stockService.updatePricesFromCsv();
    }

    @PostMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void test() {
        stockService.test();
    }
}
