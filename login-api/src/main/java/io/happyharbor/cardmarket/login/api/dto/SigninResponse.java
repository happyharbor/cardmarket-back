package io.happyharbor.cardmarket.login.api.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class SigninResponse {
    @NonNull
    String token;
}
