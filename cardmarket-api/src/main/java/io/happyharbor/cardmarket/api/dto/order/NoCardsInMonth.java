package io.happyharbor.cardmarket.api.dto.order;

import lombok.Value;

import java.time.Month;

@Value
public class NoCardsInMonth {
    int numberOfCards;
    Month month;
}
