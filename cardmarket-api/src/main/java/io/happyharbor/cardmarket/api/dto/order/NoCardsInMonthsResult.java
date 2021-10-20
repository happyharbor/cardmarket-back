package io.happyharbor.cardmarket.api.dto.order;

import lombok.*;

import java.util.List;

@Value
public class NoCardsInMonthsResult {
    List<NoCardsInMonth> noCardsInMonth;
}
