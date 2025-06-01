package com.influmatch.notifications.api;

import com.influmatch.notifications.domain.model.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

@Schema(description = "Respuesta con información de una notificación")
public record NotificationResponse(
    @Schema(description = "ID único de la notificación", example = "1")
    Long id,

    @Schema(description = "ID del usuario destinatario", example = "1")
    Long userId,

    @Schema(description = "Tipo de notificación", example = "MSG")
    NotificationType type,

    @Schema(description = "Datos contextuales de la notificación",
           example = "{\"messageId\": 123, \"dialogId\": 456, \"text\": \"Nuevo mensaje\"}")
    Map<String, Object> payload,

    @Schema(description = "Fecha y hora de creación", example = "2024-03-01T10:15:30Z")
    Instant createdAt,

    @Schema(description = "Fecha y hora de lectura (null si no ha sido leída)", 
           example = "2024-03-01T10:16:00Z", nullable = true)
    Instant readAt
) {} 