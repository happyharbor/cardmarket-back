package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActorType {

    SELLER("seller", 1),
    BUYER("buyer", 2);

    @JsonValue
    private final String name;
    private final int value;
}
