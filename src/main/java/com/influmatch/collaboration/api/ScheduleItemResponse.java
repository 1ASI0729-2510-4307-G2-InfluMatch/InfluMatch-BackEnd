package com.influmatch.collaboration.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Detalles de un ítem de cronograma")
public record ScheduleItemResponse(
    @Schema(description = "ID único del ítem", example = "1")
    Long id,

    @Schema(description = "ID de la campaña asociada", example = "123")
    Long campaignId,

    @Schema(description = "Título/descripción de la tarea", example = "Entrega de material fotográfico")
    String title,

    @Schema(description = "Fecha límite de entrega", example = "2024-06-15")
    LocalDate dueDate,

    @Schema(description = "Fecha de completado (null si pendiente)", example = "2024-05-31T12:00:00")
    LocalDateTime doneAt,

    @Schema(description = "Fecha de creación", example = "2024-05-31T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "Fecha de última actualización", example = "2024-05-31T12:00:00")
    LocalDateTime updatedAt
) {} 