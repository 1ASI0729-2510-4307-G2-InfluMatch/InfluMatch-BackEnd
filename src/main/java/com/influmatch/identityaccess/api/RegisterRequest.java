// src/main/java/com/influmatch/identityaccess/api/RegisterRequest.java
package com.influmatch.identityaccess.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Registration request DTO with strong validation rules.
 * - Email must be valid format
 * - Password must be at least 8 chars with mix of upper/lower/numbers/special
 * - Role must be either BRAND or INFLUENCER
 */
@Schema(description = "Datos necesarios para registrar un nuevo usuario")
public record RegisterRequest(
        @Schema(
            description = "Email del usuario",
            example = "empresa@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "email_required")
        @Email(message = "email_invalid", 
              regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        String email,

        @Schema(
            description = "Contraseña del usuario (mínimo 8 caracteres, debe incluir mayúsculas, minúsculas, números y caracteres especiales)",
            example = "Test123#@",
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "password_required")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "password_weak")
        String password,

        @Schema(
            description = "Rol del usuario",
            example = "BRAND",
            allowableValues = {"BRAND", "INFLUENCER"},
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "role_required")
        Role role
) {
    public enum Role { 
        @Schema(description = "Rol para empresas/marcas")
        BRAND, 
        @Schema(description = "Rol para influencers")
        INFLUENCER 
    }

    /**
     * Validates that the password meets security requirements:
     * - At least 8 characters
     * - Contains at least one digit
     * - Contains at least one lowercase letter
     * - Contains at least one uppercase letter
     * - Contains at least one special character
     * - No whitespace allowed
     */
    public boolean isPasswordValid() {
        return password != null && 
               password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }
}
