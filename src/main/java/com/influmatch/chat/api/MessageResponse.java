package com.influmatch.chat.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Detalles de un mensaje")
public record MessageResponse(
    @Schema(description = "ID único del mensaje", example = "1")
    Long id,

    @Schema(description = "ID del remitente", example = "123")
    Long senderId,

    @Schema(description = "Contenido del mensaje", example = "¡Hola! ¿Cómo estás?")
    String content,

    @Schema(description = "Fecha de lectura (null si no leído)", example = "2024-05-31T12:00:00")
    LocalDateTime readAt,

    @Schema(description = "Fecha de creación", example = "2024-05-31T12:00:00")
    LocalDateTime createdAt
) {} 