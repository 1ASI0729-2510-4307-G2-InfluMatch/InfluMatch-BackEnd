package com.influmatch.collaboration.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de error estandarizada")
public record ErrorResponse(
    @Schema(description = "Código de error", example = "collaboration_request_not_found")
    String error,
    
    @Schema(description = "Mensaje descriptivo del error", example = "La solicitud no existe")
    String message
) {} 