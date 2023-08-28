package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class PriceGuide {
    @JsonAlias("SELL")
    BigDecimal sell;
    @JsonAlias("LOW")
    BigDecimal low;
    @JsonAlias("LOWEX")
    BigDecimal lowEx;
    @JsonAlias("LOWFOIL")
    BigDecimal lowFoil;
    @JsonAlias("AVG")
    BigDecimal avg;
    @JsonAlias("TREND")
    BigDecimal trend;
    @JsonAlias("TRENDFOIL")
    BigDecimal trendFoil;
}
