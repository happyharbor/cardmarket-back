package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Price {
    @JsonProperty("price")
    BigDecimal articlePrice;
    @JsonProperty("idCurrency")
    int currencyId;
    String currencyCode;
}
