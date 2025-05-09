package com.aviralgupta.site.monitoring_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserAuthDto {

    @NotNull(message = "Email field is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Password field is required")
    @NotBlank
    private String password;
}
