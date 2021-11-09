package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderState {

    BOUGHT("bought", 1),
    PAID("paid", 2),
    SENT("sent", 4),
    RECEIVED("received", 8),
    LOST("lost", 32),
    CANCELLED("cancelled", 128);

    @JsonValue
    private final String name;
    private final int value;
}
