package io.happyharbor.cardmarket.login.api.service;

import io.happyharbor.cardmarket.login.api.dto.*;

public interface UserService {
    SigninResponse login(final LoginRequest loginRequest);
    SignupResponse register(final RegisterRequest registerRequest);
    WhoAmIResponse whoAmI(final String username);
}
