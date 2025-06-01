package com.influmatch.ratings.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Solicitud para crear una nueva valoración")
public record CreateRatingRequest(
    @Schema(description = "ID del usuario destinatario", example = "1")
    @NotNull(message = "El ID del destinatario es requerido")
    Long targetId,

    @Schema(description = "ID de la campaña asociada (opcional)", example = "1", nullable = true)
    Long campaignId,

    @Schema(description = "Puntuación (1-5)", example = "4")
    @NotNull(message = "La puntuación es requerida")
    @Min(value = 1, message = "La puntuación debe ser al menos 1")
    @Max(value = 5, message = "La puntuación no puede ser mayor a 5")
    Integer score,

    @Schema(description = "Comentario", example = "Excelente trabajo y comunicación", nullable = true)
    @Size(max = 1000, message = "El comentario no puede exceder los 1000 caracteres")
    String comment
) {} 