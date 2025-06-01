package com.influmatch.shared.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de error")
public record ErrorResponse(
    @Schema(description = "Código de error", example = "dialog_not_found")
    String error,

    @Schema(description = "Mensaje descriptivo", example = "El diálogo no existe")
    String message
) {} 