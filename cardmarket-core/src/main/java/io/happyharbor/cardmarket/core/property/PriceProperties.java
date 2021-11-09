package io.happyharbor.cardmarket.core.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "price")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class PriceProperties {
    private final BigDecimal percentageOfPowersellers;
    private final BigDecimal changePriceThreshold;
    private final Double tooHighPriceThreshold;
}
