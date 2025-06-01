package com.influmatch.collaboration.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear una nueva solicitud de colaboración")
public record CreateCollaborationRequest(
    @Schema(description = "ID del usuario al que se enviará la solicitud", example = "123")
    @NotNull(message = "to_user_required")
    Long toUserId,

    @Schema(description = "Mensaje opcional para el destinatario", example = "Me gustaría colaborar contigo en una campaña")
    @Size(max = 1000, message = "message_too_long")
    String message
) {} 