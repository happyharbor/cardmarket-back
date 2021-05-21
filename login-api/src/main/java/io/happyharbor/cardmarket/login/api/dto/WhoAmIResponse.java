package io.happyharbor.cardmarket.login.api.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class WhoAmIResponse {
    UUID id;
    String username;
    LocalDateTime createdAt;
    Set<String> roles;
}
