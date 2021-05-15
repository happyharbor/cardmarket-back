package io.happyharbor.cardmarket.api.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class MoneyDetails {
    BigDecimal totalBalance;
    BigDecimal moneyBalance;
    BigDecimal bonusBalance;
    BigDecimal unpaidAmount;
    BigDecimal providerRechargeAmount;
    @JsonProperty("idCurrency")
    int currencyId;
    String currencyCode;
}
