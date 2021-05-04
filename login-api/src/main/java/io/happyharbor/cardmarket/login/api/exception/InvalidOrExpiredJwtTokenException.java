package io.happyharbor.cardmarket.login.api.exception;

public class InvalidOrExpiredJwtTokenException extends RuntimeException{
    public InvalidOrExpiredJwtTokenException() {
        super("Expired or invalid JWT token");
    }
}
