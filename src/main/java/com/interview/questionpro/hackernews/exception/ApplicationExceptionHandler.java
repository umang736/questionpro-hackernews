package com.interview.questionpro.hackernews.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  public final ResponseEntity<ApiError> handleAplicationExceptions(ApplicationException ex) {
    return new ResponseEntity<ApiError>(ex.getApiError(), ex.getApiError().getStatus());
  }
}