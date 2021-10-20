package io.happyharbor.cardmarket.client;

import io.happyharbor.cardmarket.api.configuration.DateProvider;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import io.happyharbor.cardmarket.client.dto.stock.GetStockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClientServiceImplTest {

    ClientServiceImpl clientService;
    CardmarketClient cardmarketClientMock;
    DateProvider dateProviderMock;

    @BeforeEach
    void setUp() {
        cardmarketClientMock = Mockito.mock(CardmarketClient.class);
        dateProviderMock = Mockito.mock(DateProvider.class);
        when(dateProviderMock.provideDateTime()).thenReturn(LocalDateTime.of(2020, 10, 20, 18, 30));
        clientService = new ClientServiceImpl(cardmarketClientMock, dateProviderMock);
    }

    @Test
    void getStock_success() {
        when(cardmarketClientMock.sendGetRequest(any(), any())).thenReturn(completedFuture(GetStockResponse.builder()
                .articles(List.of(MyArticle.builder().build()))
                .build()
        ));

        final List<MyArticle> stock = clientService.getStock().join();
        assertEquals(1, stock.size());
    }
}