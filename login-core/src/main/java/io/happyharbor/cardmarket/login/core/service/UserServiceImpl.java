package io.happyharbor.cardmarket.login.core.service;

import io.happyharbor.cardmarket.login.api.dto.*;
import io.happyharbor.cardmarket.login.api.enums.Role;
import io.happyharbor.cardmarket.login.api.exception.UserNotFoundException;
import io.happyharbor.cardmarket.login.api.exception.UsernameExistException;
import io.happyharbor.cardmarket.login.api.service.UserService;
import io.happyharbor.cardmarket.login.core.entity.ApplicationUser;
import io.happyharbor.cardmarket.login.core.repository.ApplicationUserRepository;
import io.happyharbor.cardmarket.login.core.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    @Override
    public SigninResponse signin(final SigninRequest signinRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
        val user = applicationUserRepository.findByUsername(signinRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException(signinRequest.getUsername()));
        return SigninResponse.builder()
                .token(jwtTokenProvider.createToken(signinRequest.getUsername(), user.getRoles()))
                .build();
    }

    @Override
    public SignupResponse signup(final SignupRequest signupRequest) {

        if (applicationUserRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UsernameExistException(signupRequest.getUsername());
        }

        applicationUserRepository.save(ApplicationUser.builder()
                .roles(Collections.singleton(signupRequest.getRole()))
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build());
        return SignupResponse.builder()
                .token(jwtTokenProvider.createToken(signupRequest.getUsername(), Collections.singleton(signupRequest.getRole())))
                .build();
    }

    @Override
    public WhoAmIResponse whoAmI(final String username) {
        val user = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return WhoAmIResponse.builder()
                .id(user.getId())
                .createdAt(user.getCreateTs())
                .username(username)
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
}
