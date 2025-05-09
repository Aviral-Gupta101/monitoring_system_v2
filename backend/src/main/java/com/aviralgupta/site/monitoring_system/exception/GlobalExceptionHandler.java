package com.aviralgupta.site.monitoring_system.exception;

import com.aviralgupta.site.monitoring_system.exception.custom_exceptions.InvalidUserCredentialsException;
import com.aviralgupta.site.monitoring_system.exception.custom_exceptions.NotFoundException;
import com.aviralgupta.site.monitoring_system.exception.custom_exceptions.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e){
        log.error("Exception: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(InvalidUserCredentialsException.class)
    public ResponseEntity<?> invalidUserCredentialsExceptionHandler(InvalidUserCredentialsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsExceptionHandler(UserAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundExceptionHandler(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
    }

}
