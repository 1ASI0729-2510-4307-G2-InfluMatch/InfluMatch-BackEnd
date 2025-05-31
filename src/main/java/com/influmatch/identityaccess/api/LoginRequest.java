package com.influmatch.identityaccess.api;

import jakarta.validation.constraints.*;

/**
 * Login request DTO with validation rules
 */
public record LoginRequest(
    @NotBlank(message = "email_required")
    @Email(message = "email_invalid")
    String email,

    @NotBlank(message = "password_required")
    String password
) {} 