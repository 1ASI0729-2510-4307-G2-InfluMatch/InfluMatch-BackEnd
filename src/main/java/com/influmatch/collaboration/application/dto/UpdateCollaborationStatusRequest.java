package com.influmatch.collaboration.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar el estado de una colaboración")
public class UpdateCollaborationStatusRequest {
    @Schema(description = "Acción a realizar", example = "ACCEPT", allowableValues = {"ACCEPT", "REJECT", "FINISH"})
    @NotBlank(message = "Action is required")
    private String action;
} 