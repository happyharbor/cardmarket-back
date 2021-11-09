package io.happyharbor.cardmarket.rest.controller;

import io.happyharbor.cardmarket.api.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    public void updatePrices() {
        stockService.updatePrices();
    }

    @PutMapping("/update-prices-from-csv")
    public void updatePricesFromCsv() {
        stockService.updatePricesFromCsv();
    }
}
