package io.happyharbor.cardmarket.rest.exception;

import io.happyharbor.cardmarket.login.api.exception.UserNotFoundException;
import io.happyharbor.cardmarket.login.api.exception.UsernameExistException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

  @ExceptionHandler(AccessDeniedException.class)
  public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
  }

  @ExceptionHandler(UserNotFoundException.class)
  public void handleException(HttpServletResponse res, UserNotFoundException ex) throws IOException {
    res.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

  @ExceptionHandler(UsernameExistException.class)
  public void handleException(HttpServletResponse res, UsernameExistException ex) throws IOException {
    res.sendError(HttpStatus.CONFLICT.value(), ex.getMessage());
  }

}
