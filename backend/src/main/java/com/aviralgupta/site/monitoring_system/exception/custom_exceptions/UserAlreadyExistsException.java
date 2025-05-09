package com.aviralgupta.site.monitoring_system.exception.custom_exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
