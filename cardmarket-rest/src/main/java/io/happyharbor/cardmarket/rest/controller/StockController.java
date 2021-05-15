package io.happyharbor.cardmarket.rest.controller;

import io.happyharbor.cardmarket.api.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
@Log4j2
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
