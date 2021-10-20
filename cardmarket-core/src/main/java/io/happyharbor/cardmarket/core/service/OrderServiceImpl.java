package io.happyharbor.cardmarket.core.service;

import io.happyharbor.cardmarket.api.configuration.DateProvider;
import io.happyharbor.cardmarket.api.dto.order.*;
import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.api.service.OrderService;
import io.happyharbor.cardmarket.core.csv.CsvHelper;
import io.happyharbor.cardmarket.core.csv.CsvOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Collections;
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
    private final DateProvider dateProvider;

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

    @Override
    public CompletableFuture<NoCardsInMonthsResult> getNumberOfCardsIn(final NoCardsInMonthsRequest request) {
        log.debug("Get number of cards in {}", request);

        if (request.getMonths().isEmpty()) {
            return CompletableFuture.completedFuture(new NoCardsInMonthsResult(Collections.emptyList()));
        }
        val orders = clientService.getOrdersByMax1YearBack(FilteredOrdersRequest.builder()
                        .actor(ActorType.SELLER)
                        .state(OrderState.SENT)
                        .build())
                .join();
        orders.addAll(clientService.getOrdersByMax1YearBack(FilteredOrdersRequest.builder()
                        .actor(ActorType.SELLER)
                        .state(OrderState.ARRIVED)
                        .build())
                .join());

        orders.addAll(clientService.getOrdersByMax1YearBack(FilteredOrdersRequest.builder()
                        .actor(ActorType.SELLER)
                        .state(OrderState.NOT_ARRIVED)
                        .build())
                .join());

        val noCardsInMonthsResult = orders.stream()
                .map(order -> {
                    val stateDto = order.getState().toBuilder().dateSent(order.getState().getDateSent()
                                    .withDayOfMonth(1)
                                    .withHour(0)
                                    .withMinute(0)
                                    .withSecond(0))
                            .build();
                    return order.toBuilder().state(stateDto).build();
                })
                .collect(Collectors.groupingBy(order -> order.getState().getDateSent()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAfter(dateProvider.provideDateTime().minusYears(1)) &&
                                 request.getMonths().contains(entry.getKey().getMonth()))
                .map(entry -> new NoCardsInMonth(entry.getValue().stream().mapToInt(Order::getArticleCount).sum(),
                                                 entry.getKey().getMonth()))
                .collect(Collectors.toList());

        log.info("Got number of cards in {} and are: {}", request, noCardsInMonthsResult);
        return CompletableFuture.completedFuture(new NoCardsInMonthsResult(noCardsInMonthsResult));
    }
}
