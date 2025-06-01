package com.influmatch.identityaccess.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de error")
public record ErrorResponse(
    @Schema(description = "Código de error", example = "invalid_credentials")
    String error,

    @Schema(description = "Mensaje descriptivo del error", example = "Credenciales inválidas")
    String message
) {
    public static ErrorResponse of(String error, String message) {
        return new ErrorResponse(error, message);
    }
} 