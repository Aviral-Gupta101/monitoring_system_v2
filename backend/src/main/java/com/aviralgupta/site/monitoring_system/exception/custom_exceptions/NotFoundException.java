package com.aviralgupta.site.monitoring_system.exception.custom_exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
