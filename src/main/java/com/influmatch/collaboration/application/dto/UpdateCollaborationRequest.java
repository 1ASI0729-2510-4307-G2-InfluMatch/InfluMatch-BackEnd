package com.influmatch.collaboration.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar una colaboración")
public class UpdateCollaborationRequest {
    @Schema(description = "Mensaje", example = "¡Hola! Me gustaría colaborar contigo...")
    private String message;

    @Schema(description = "Fecha objetivo", example = "2025-07-12")
    private String targetDate;

    @Schema(description = "Tipo de acción", example = "POST_IG")
    private String actionType;

    @Schema(description = "Presupuesto", example = "500.00")
    @DecimalMin(value = "0.01", message = "Budget must be greater than zero")
    private BigDecimal budget;

    @Schema(description = "Lista de hitos o eventos")
    @Valid
    private List<MilestoneDto> milestones;

    @Schema(description = "Ubicación o enlace de reunión", example = "https://meet.link/abc")
    private String location;

    @Schema(description = "Entregables esperados", example = "1 reel + 3 stories")
    private String deliverables;
} 