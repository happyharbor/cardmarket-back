package io.happyharbor.cardmarket.api.dto.account;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class BankAccount {
    String accountOwner;
    String iban;
    String bic;
    String bankName;
}
