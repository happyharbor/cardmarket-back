package io.happyharbor.cardmarket.client.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.happyharbor.cardmarket.api.dto.account.Account;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAccountResponse {
    Account account;
}
