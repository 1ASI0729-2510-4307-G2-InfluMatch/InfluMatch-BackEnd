package com.influmatch.auth.application.dto;

import com.influmatch.auth.domain.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para registrar un nuevo usuario")
public class RegisterRequest {
    @Schema(description = "Email del usuario", example = "user@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "password123")
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "Rol del usuario (BRAND o INFLUENCER)", example = "BRAND")
    @NotNull(message = "Role is required")
    private UserRole role;
} 