package com.influmatch.collaboration.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Datos para crear un nuevo ítem de cronograma")
public record CreateScheduleItemRequest(
    @Schema(description = "Título/descripción de la tarea", example = "Entrega de material fotográfico")
    @NotBlank(message = "title_required")
    @Size(max = 200, message = "title_too_long")
    String title,

    @Schema(description = "Fecha límite de entrega", example = "2024-06-15")
    @NotNull(message = "due_date_required")
    @Future(message = "due_date_must_be_future")
    LocalDate dueDate
) {} 