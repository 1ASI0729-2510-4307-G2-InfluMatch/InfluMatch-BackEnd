package com.influmatch.collaboration.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear una nueva colaboración")
public class CreateCollaborationRequest {
    @Schema(description = "ID del usuario de la contraparte")
    @NotNull(message = "Counterpart ID is required")
    private Long counterpartId;

    @Schema(description = "Mensaje inicial", example = "¡Hola! Me gustaría colaborar contigo...")
    @NotBlank(message = "Message is required")
    private String message;

    @Schema(description = "Tipo de acción", example = "POST_IG")
    @NotNull(message = "Action type is required")
    private String actionType;

    @Schema(description = "Fecha objetivo", example = "2025-07-12")
    @NotNull(message = "Target date is required")
    private String targetDate;

    @Schema(description = "Presupuesto", example = "500.00")
    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.01", message = "Budget must be greater than zero")
    private BigDecimal budget;

    @Schema(description = "Lista de hitos o eventos")
    @Valid
    @Builder.Default
    private List<MilestoneDto> milestones = new ArrayList<>();

    @Schema(description = "Ubicación o enlace de reunión", example = "https://meet.link/abc")
    private String location;

    @Schema(description = "Entregables esperados", example = "1 reel + 3 stories")
    private String deliverables;
} 