package io.happyharbor.cardmarket.core.service;

import io.happyharbor.cardmarket.api.dto.order.ActorType;
import io.happyharbor.cardmarket.api.dto.order.FilteredOrdersRequest;
import io.happyharbor.cardmarket.api.dto.order.OrderState;
import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.api.service.OrderService;
import io.happyharbor.cardmarket.core.csv.CsvHelper;
import io.happyharbor.cardmarket.core.csv.CsvOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ClientService clientService;
    private final CsvHelper csvHelper;

    @Override
    public CompletableFuture<OutputStream> getShippingAddresses() {
        return clientService.getOrdersBy(FilteredOrdersRequest.builder()
                                                              .actor(ActorType.SELLER)
                                                              .state(OrderState.PAID)
                                                              .build())
                .thenApply(orders -> orders.stream().map(o -> CsvOrder.builder()
                                                                      .orderId(o.getOrderId())
                                                                      .name(o.getShippingAddress().getName())
                                                                      .extra(o.getShippingAddress().getExtra())
                                                                      .street(o.getShippingAddress().getStreet())
                                                                      .postCode(o.getShippingAddress().getPostCode())
                                                                      .city(o.getShippingAddress().getCity())
                                                                      .country(o.getShippingAddress().getCountry().getName())
                                                                      .build())
                        .collect(Collectors.toList()))
                .thenApply(csvHelper::writeCsv);

    }
}
