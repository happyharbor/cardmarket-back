package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ProductDetailedExpansion {
    @JsonAlias("idExpansion")
    Integer id;
    String enName;
    Integer expansionIcon;
}
