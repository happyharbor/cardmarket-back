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
    private final Integer pricesToKeep;
    /**
     * For example 1.33 means that an article that is 33% more expensive comparing to cardmarket will not be updated
     */
    private final BigDecimal changePriceThreshold;
    /**
     * Any article with less than this price will be updated independent of {@link #getChangePriceThreshold()}
     */
    private final BigDecimal tooHighPriceThreshold;
}
