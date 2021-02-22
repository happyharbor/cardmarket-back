package io.happyharbor.cardmarket.client;

import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import io.happyharbor.cardmarket.client.dto.GetStockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClientServiceImplTest {

    ClientServiceImpl clientService;
    CardmarketClient cardmarketClientMock;

    @BeforeEach
    void setUp() {
        cardmarketClientMock = Mockito.mock(CardmarketClient.class);
        clientService = new ClientServiceImpl(cardmarketClientMock);
    }

    @Test
    void getStock_success() {
        when(cardmarketClientMock.sendGetRequest(any(), any())).thenReturn(completedFuture(GetStockResponse.builder()
                .articles(List.of(MyArticle.builder().build()))
                .build()
        ));

        final List<MyArticle> stock = clientService.getStock().join();
        assertEquals(10, stock.size());
    }
}