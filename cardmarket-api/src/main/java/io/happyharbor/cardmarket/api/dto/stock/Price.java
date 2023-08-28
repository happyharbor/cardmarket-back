package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Price {
    @JsonAlias("price")
    BigDecimal articlePrice;
    @JsonAlias("idCurrency")
    int currencyId;
    String currencyCode;
}
