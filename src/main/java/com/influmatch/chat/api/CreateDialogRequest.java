package com.influmatch.chat.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Schema(description = "Datos para crear un nuevo diálogo")
public record CreateDialogRequest(
    @Schema(description = "ID de la campaña asociada (opcional)", example = "123")
    Long campaignId,

    @Schema(description = "IDs de los participantes iniciales", example = "[1, 2]")
    @NotEmpty(message = "participants_required")
    @Size(min = 2, message = "min_two_participants")
    Set<Long> participantIds
) {} 