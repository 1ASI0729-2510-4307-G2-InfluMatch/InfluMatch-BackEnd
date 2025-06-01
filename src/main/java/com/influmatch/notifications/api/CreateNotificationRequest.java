package com.influmatch.notifications.api;

import com.influmatch.notifications.domain.model.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

@Schema(description = "Solicitud para crear una nueva notificación")
public record CreateNotificationRequest(
    @Schema(description = "ID del usuario destinatario", example = "1")
    @NotNull(message = "El ID del usuario es requerido")
    Long userId,

    @Schema(description = "Tipo de notificación", example = "MSG")
    @NotNull(message = "El tipo de notificación es requerido")
    NotificationType type,

    @Schema(description = "Datos contextuales de la notificación", 
           example = "{\"messageId\": 123, \"dialogId\": 456, \"text\": \"Nuevo mensaje\"}")
    Map<String, Object> payload
) {} 