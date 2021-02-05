package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ShippingMethod {
    @JsonProperty("idShippingMethod")
    Integer shippingMethodId;
    String name;
    BigDecimal price;
    @JsonProperty("idCurrency")
    Integer currencyId;
    String currencyCode;
    Boolean isLetter;
    @JsonProperty("isInsured")
    boolean isTracked;
}
