package io.happyharbor.cardmarket.client.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.happyharbor.cardmarket.api.dto.account.Account;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetVacationResponse {
    Account account;
    String message;
}
