package com.influmatch.collaboration.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos completos de una colaboración")
public class CollaborationDetailDto {
    @Schema(description = "ID de la colaboración")
    private Long id;

    @Schema(description = "ID del usuario que envió la solicitud")
    private Long initiatorId;

    @Schema(description = "ID del usuario que recibió la solicitud")
    private Long counterpartId;

    @Schema(description = "Estado actual", example = "PENDING")
    private String status;

    @Schema(description = "Rol del iniciador", example = "BRAND")
    private String initiatorRole;

    @Schema(description = "Información de la contraparte")
    private CounterpartDto counterpart;

    @Schema(description = "Mensaje", example = "¡Hola! Me gustaría colaborar contigo...")
    private String message;

    @Schema(description = "Tipo de acción", example = "POST_IG")
    private String actionType;

    @Schema(description = "Fecha objetivo", example = "2025-07-12")
    private String targetDate;

    @Schema(description = "Presupuesto", example = "500.00")
    private BigDecimal budget;

    @Schema(description = "Lista de hitos o eventos")
    @Builder.Default
    private List<MilestoneDto> milestones = new ArrayList<>();

    @Schema(description = "Ubicación o enlace de reunión", example = "https://meet.link/abc")
    private String location;

    @Schema(description = "Entregables esperados", example = "1 reel + 3 stories")
    private String deliverables;

    @Schema(description = "Fecha de creación")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;
} 