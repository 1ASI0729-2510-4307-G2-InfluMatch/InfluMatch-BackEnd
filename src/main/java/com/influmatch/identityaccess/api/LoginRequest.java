package com.influmatch.identityaccess.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Login request DTO with validation rules
 */
@Schema(description = "Datos necesarios para iniciar sesión")
public record LoginRequest(
    @Schema(
        description = "Email del usuario",
        example = "usuario@empresa.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "email_required")
    @Email(message = "email_invalid")
    String email,

    @Schema(
        description = "Contraseña del usuario",
        example = "Test123#@",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "password_required")
    String password
) {} 