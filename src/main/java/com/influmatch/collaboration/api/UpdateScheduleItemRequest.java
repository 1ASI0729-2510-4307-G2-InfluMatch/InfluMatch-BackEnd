package com.influmatch.collaboration.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Datos para actualizar un ítem de cronograma")
public record UpdateScheduleItemRequest(
    @Schema(description = "Nuevo título/descripción", example = "Entrega de material fotográfico")
    @Size(max = 200, message = "title_too_long")
    String title,

    @Schema(description = "Nueva fecha límite", example = "2024-06-15")
    @Future(message = "due_date_must_be_future")
    LocalDate dueDate,

    @Schema(description = "Fecha de completado (null si pendiente)", example = "2024-05-31T12:00:00")
    LocalDateTime doneAt
) {} 