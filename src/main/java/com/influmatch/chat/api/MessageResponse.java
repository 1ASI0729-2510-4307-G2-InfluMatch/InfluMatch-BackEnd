package com.influmatch.chat.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Respuesta con información de un mensaje")
public record MessageResponse(
    @Schema(description = "ID único del mensaje", example = "1")
    Long id,

    @Schema(description = "ID del diálogo al que pertenece el mensaje", example = "1")
    Long dialogId,

    @Schema(description = "ID del usuario que envió el mensaje", example = "1")
    Long senderId,

    @Schema(description = "Contenido del mensaje", example = "¡Hola! ¿Cómo estás?")
    String content,

    @Schema(description = "ID del archivo adjunto (si existe)", example = "1", nullable = true)
    Long assetId,

    @Schema(description = "Fecha y hora de creación", example = "2024-03-01T10:15:30Z")
    Instant createdAt,

    @Schema(description = "Fecha y hora de lectura (null si no ha sido leído)", 
           example = "2024-03-01T10:16:00Z", nullable = true)
    Instant readAt
) {} 