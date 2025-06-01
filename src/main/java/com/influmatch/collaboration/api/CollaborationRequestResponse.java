package com.influmatch.collaboration.api;

import com.influmatch.collaboration.domain.model.CollaborationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Detalles de una solicitud de colaboración")
public record CollaborationRequestResponse(
    @Schema(description = "ID único de la solicitud", example = "1")
    Long id,

    @Schema(description = "ID del usuario que envió la solicitud", example = "123")
    Long fromUserId,

    @Schema(description = "ID del usuario que recibió la solicitud", example = "456")
    Long toUserId,

    @Schema(description = "Estado actual de la solicitud", example = "PENDING")
    CollaborationStatus status,

    @Schema(description = "Mensaje de la solicitud", example = "Me gustaría colaborar contigo en una campaña")
    String message,

    @Schema(description = "Fecha de creación", example = "2024-05-31T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "Fecha de última actualización", example = "2024-05-31T12:00:00")
    LocalDateTime updatedAt
) {} 