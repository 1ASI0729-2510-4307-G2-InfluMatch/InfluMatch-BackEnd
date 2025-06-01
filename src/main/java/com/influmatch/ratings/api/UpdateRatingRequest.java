package com.influmatch.ratings.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Schema(description = "Solicitud para actualizar una valoración existente")
public record UpdateRatingRequest(
    @Schema(description = "Nueva puntuación (1-5)", example = "4", nullable = true)
    @Min(value = 1, message = "La puntuación debe ser al menos 1")
    @Max(value = 5, message = "La puntuación no puede ser mayor a 5")
    Integer score,

    @Schema(description = "Nuevo comentario", example = "Excelente trabajo y comunicación", nullable = true)
    @Size(max = 1000, message = "El comentario no puede exceder los 1000 caracteres")
    String comment
) {} 