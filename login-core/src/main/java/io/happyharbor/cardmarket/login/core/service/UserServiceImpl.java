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
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    @Override
    public SigninResponse login(final LoginRequest loginRequest) {
        log.debug("Trying to authenticate user: {}", loginRequest);
        val principal = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        val roles = principal.getAuthorities()
                .stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .collect(Collectors.toSet());
        log.info("User: {} was authenticated successfully", loginRequest);
        return SigninResponse.builder()
                .token(jwtTokenProvider.createToken(loginRequest.getUsername(), roles))
                .build();
    }

    @Override
    public SignupResponse register(final RegisterRequest registerRequest) {
        log.debug("Trying to register user {}", registerRequest);
        if (applicationUserRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameExistException(registerRequest.getUsername());
        }

        applicationUserRepository.save(ApplicationUser.builder()
                .roles(Collections.singleton(registerRequest.getRole()))
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build());
        log.info("User {} was registered successfully", registerRequest);
        return SignupResponse.builder()
                .token(jwtTokenProvider.createToken(registerRequest.getUsername(), Collections.singleton(new SimpleGrantedAuthority(registerRequest.getRole().getGrantedAuthorityName()))))
                .build();
    }

    @Override
    public WhoAmIResponse whoAmI(final String username) {
        log.debug("Asking who is user {}", username);
        val user = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        log.info("Asked successfully about {}", user);
        return WhoAmIResponse.builder()
                .id(user.getId())
                .createdAt(user.getCreateTs())
                .username(username)
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
}
