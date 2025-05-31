// src/main/java/com/influmatch/identityaccess/api/RegisterResponse.java
package com.influmatch.identityaccess.api;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for successful registration
 */
@Schema(description = "Respuesta exitosa del registro de usuario")
public record RegisterResponse(
    @Schema(description = "ID único del usuario registrado", example = "1")
    Long userId,

    @Schema(description = "Email del usuario registrado", example = "empresa@example.com")
    String email,

    @Schema(description = "Rol asignado al usuario", example = "BRAND", allowableValues = {"BRAND", "INFLUENCER"})
    String role,

    @Schema(
        description = "Token JWT para autenticación inmediata",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    String token,

    @Schema(description = "Mensaje de éxito", example = "Registro exitoso!")
    String message
) {}
