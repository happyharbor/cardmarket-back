package io.happyharbor.cardmarket.login.api.dto;

import io.happyharbor.cardmarket.login.api.enums.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class SignupRequest {
    @NotBlank(message = "Username cannot be empty")
    String username;

    @Size(min = 8, message = "Password needs to have at least 8 characters")
    @Pattern(regexp = ".*[a-z].*", message = "Password needs to have at least one small letter")
    @Pattern(regexp = ".*[A-Z].*", message = "Password needs to have at least one capital letter")
    @Pattern(regexp = ".*[\\d].*", message = "Password needs to have at least one number")
    @Pattern(regexp = ".*[~!@#$%^&*()_+].*", message = "Password needs to have at least one special character")
    String password;

    @NotNull
    Role role;
}
