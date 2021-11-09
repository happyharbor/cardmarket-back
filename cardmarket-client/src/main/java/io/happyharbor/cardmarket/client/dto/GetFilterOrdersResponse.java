package io.happyharbor.cardmarket.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.happyharbor.cardmarket.api.dto.order.Order;
import lombok.*;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetFilterOrdersResponse {
    @JsonProperty("order")
    List<Order> orders;
}
