package com.influmatch.chat.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Schema(description = "Detalles de un diálogo")
public record DialogResponse(
    @Schema(description = "ID único del diálogo", example = "1")
    Long id,

    @Schema(description = "ID de la campaña asociada (si existe)", example = "123")
    Long campaignId,

    @Schema(description = "IDs de los participantes", example = "[1, 2]")
    Set<Long> participantIds,

    @Schema(description = "Últimos mensajes del diálogo")
    List<MessageResponse> recentMessages,

    @Schema(description = "Fecha de creación", example = "2024-05-31T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "Fecha de última actualización", example = "2024-05-31T12:00:00")
    LocalDateTime updatedAt
) {} 