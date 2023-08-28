package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ShippingMethod {
    @JsonAlias("idShippingMethod")
    Integer shippingMethodId;
    String name;
    BigDecimal price;
    @JsonAlias("idCurrency")
    Integer currencyId;
    String currencyCode;
    Boolean isLetter;
    @JsonAlias("isInsured")
    boolean isTracked;
}
