package io.happyharbor.cardmarket.login.api.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(final String username) {
        super(String.format("User: %s was not found", username));
    }
}
