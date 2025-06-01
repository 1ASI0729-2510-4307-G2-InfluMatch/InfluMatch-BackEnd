package com.influmatch.ratings.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Respuesta con información de una valoración")
public record RatingResponse(
    @Schema(description = "ID único de la valoración", example = "1")
    Long id,

    @Schema(description = "ID del usuario que escribió la valoración", example = "1")
    Long writerId,

    @Schema(description = "ID del usuario que recibió la valoración", example = "2")
    Long targetId,

    @Schema(description = "ID de la campaña asociada (si existe)", example = "1", nullable = true)
    Long campaignId,

    @Schema(description = "Puntuación (1-5)", example = "4")
    Integer score,

    @Schema(description = "Comentario", example = "Excelente trabajo y comunicación", nullable = true)
    String comment,

    @Schema(description = "Fecha y hora de creación", example = "2024-03-01T10:15:30Z")
    Instant createdAt,

    @Schema(description = "Fecha y hora de última actualización", example = "2024-03-01T10:15:30Z")
    Instant updatedAt
) {} 