package com.influmatch.collaboration.api;

import com.influmatch.collaboration.domain.model.CollaborationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para actualizar el estado de una solicitud")
public record UpdateCollaborationRequest(
    @Schema(
        description = "Nuevo estado de la solicitud", 
        example = "ACCEPTED",
        allowableValues = {"ACCEPTED", "DECLINED", "CANCELED"}
    )
    @NotNull(message = "status_required")
    CollaborationStatus status
) {} 