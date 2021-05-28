package io.happyharbor.cardmarket.rest.controller;

import io.happyharbor.cardmarket.login.api.dto.*;
import io.happyharbor.cardmarket.login.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/login")
  public SigninResponse login(@RequestBody @Valid final LoginRequest loginRequest) {
    return userService.login(loginRequest);
  }

  @PostMapping("/register")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public SignupResponse register(@RequestBody @Valid final RegisterRequest registerRequest) {
    return userService.register(registerRequest);
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public WhoAmIResponse whoAmI(final Principal principal) {
    return userService.whoAmI(principal.getName());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

    return ex.getBindingResult().getAllErrors().stream()
            .collect(Collectors.groupingBy(a -> ((FieldError) a).getField()))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(";"))));
  }
}
