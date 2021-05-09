package io.happyharbor.cardmarket.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.happyharbor.cardmarket.api.dto.market.ProductDetailed;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProductResponse {
    ProductDetailed product;
}
