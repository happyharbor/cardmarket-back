package io.happyharbor.cardmarket.rest;

import io.happyharbor.cardmarket.rest.controller.StockController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CardmarketRestApplicationTest {

    @Autowired
    private StockController stockController;

    @Test
    void contextLoads() {
        assertNotNull(stockController);
    }
}
