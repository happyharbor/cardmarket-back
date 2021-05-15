package io.happyharbor.cardmarket.core.service;

import io.happyharbor.cardmarket.api.dto.order.ActorType;
import io.happyharbor.cardmarket.api.dto.order.FilteredOrdersRequest;
import io.happyharbor.cardmarket.api.dto.order.Order;
import io.happyharbor.cardmarket.api.dto.order.OrderState;
import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.api.service.OrderService;
import io.happyharbor.cardmarket.core.csv.CsvHelper;
import io.happyharbor.cardmarket.core.csv.CsvOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ClientService clientService;
    private final CsvHelper csvHelper;

    @Override
    public CompletableFuture<OutputStream> getShippingAddresses() {
        log.info("Get Shipping Addresses");
        return clientService.getOrdersBy(FilteredOrdersRequest.builder()
                                                              .actor(ActorType.SELLER)
                                                              .state(OrderState.PAID)
                                                              .build())
                .thenApply(orders -> orders.stream()
                        .sorted(Comparator.comparing(o -> o.getState().getDatePaid()))
                        .map(Order::getShippingAddress)
                        .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .map(o -> CsvOrder.builder()
                                          .orderNum(o.getValue() == 1 ? null : Math.toIntExact(o.getValue()))
                                          .name(o.getKey().getName())
                                          .extra(o.getKey().getExtra())
                                          .street(o.getKey().getStreet())
                                          .postCode(o.getKey().getPostCode())
                                          .city(o.getKey().getCity())
                                          .country(o.getKey().getCountry().getName())
                                          .build())
                        .collect(Collectors.toList()))
                .thenApply(csvHelper::writeCsv);
    }
}
