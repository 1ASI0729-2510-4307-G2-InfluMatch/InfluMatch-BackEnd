package com.influmatch.media.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de error estandarizada")
public record ErrorResponse(
    @Schema(description = "Código de error", example = "media_not_found")
    String error,
    
    @Schema(description = "Mensaje descriptivo del error", example = "El activo multimedia no existe")
    String message
) {} 