package io.happyharbor.cardmarket.login.api.exception;

public class UsernameExistException extends RuntimeException{
    public UsernameExistException(final String username) {
        super(String.format("Username: %s already exists", username));
    }
}
