package io.happyharbor.cardmarket.login.api.service;

import io.happyharbor.cardmarket.login.api.dto.*;

public interface UserService {
    SigninResponse signin(final SigninRequest signinRequest);
    SignupResponse signup(final SignupRequest signupRequest);
    WhoAmIResponse whoAmI(final String username);
}
