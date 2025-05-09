package com.aviralgupta.site.monitoring_system.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

public class RequestValidatorUtil {

    public static ResponseEntity<?> getErrors(BindingResult bindingResult){

        Map<String, String> errors = bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : "Invalid value",
                        (msg1, msg2) -> msg1 // in case of duplicate keys
                ));

        return ResponseEntity.badRequest().body(errors);
    }
}
