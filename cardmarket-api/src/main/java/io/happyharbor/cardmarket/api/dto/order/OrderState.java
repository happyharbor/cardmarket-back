package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderState {

    UNPAID("bought", 1),
    PAID("paid", 2),
    SENT("sent", 4),
    ARRIVED("received", 8),
    NOT_ARRIVED("lost", 32),
    CANCELLED("cancelled", 128),
    EVALUATED("evaluated", 1000);

    @JsonValue
    private final String name;
    private final int value;
}
