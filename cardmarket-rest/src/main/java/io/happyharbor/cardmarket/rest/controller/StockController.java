package io.happyharbor.cardmarket.rest.controller;

import io.happyharbor.cardmarket.api.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
@Log4j2
public class StockController {

    private final StockService stockService;

    @PostMapping("/update-prices")
    public void updatePrices() {
        stockService.updatePrices();
    }
}
