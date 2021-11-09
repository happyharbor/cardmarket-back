package io.happyharbor.cardmarket.api.dto.order;

import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class FilteredOrdersRequest {
    ActorType actor;
    OrderState state;
    Integer start; // If specified, only 100 entities are returned starting from the number provided
}
