package com.influmatch.identityaccess.api;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for successful login
 */
@Schema(description = "Respuesta exitosa de inicio de sesión")
public record LoginResponse(
    @Schema(description = "ID único del usuario", example = "1")
    Long userId,

    @Schema(description = "Email del usuario", example = "usuario@empresa.com")
    String email,

    @Schema(description = "Rol del usuario", example = "BRAND", allowableValues = {"BRAND", "INFLUENCER"})
    String role,

    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token,

    @Schema(description = "Mensaje de éxito", example = "Login exitoso!")
    String message
) {} 