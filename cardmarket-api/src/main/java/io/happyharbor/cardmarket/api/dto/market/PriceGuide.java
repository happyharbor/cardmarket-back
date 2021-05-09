package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class PriceGuide {
    @JsonProperty("SELL")
    BigDecimal sell;
    @JsonProperty("LOW")
    BigDecimal low;
    @JsonProperty("LOWEX")
    BigDecimal lowEx;
    @JsonProperty("LOWFOIL")
    BigDecimal lowFoil;
    @JsonProperty("AVG")
    BigDecimal avg;
    @JsonProperty("TREND")
    BigDecimal trend;
    @JsonProperty("TRENDFOIL")
    BigDecimal trendFoil;
}
