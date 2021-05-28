package io.happyharbor.cardmarket.login.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank(message = "Username cannot be empty")
    String username;
    @NotBlank(message = "Password cannot be empty")
    String password;

    @ToString.Include(name = "password")
    private String passwordMasker() {
        return "****";
    }
}
