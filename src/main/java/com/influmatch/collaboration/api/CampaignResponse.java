package com.influmatch.collaboration.api;

import com.influmatch.collaboration.domain.model.CampaignStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Detalles de una campaña")
public record CampaignResponse(
    @Schema(description = "ID único de la campaña", example = "1")
    Long id,

    @Schema(description = "ID de la marca creadora", example = "123")
    Long brandId,

    @Schema(description = "ID del influencer asignado", example = "456")
    Long influencerId,

    @Schema(description = "Título de la campaña", example = "Campaña Verano 2024")
    String title,

    @Schema(description = "Brief/descripción de la campaña", example = "Promoción de nueva línea de productos...")
    String brief,

    @Schema(description = "Estado actual de la campaña", example = "ACTIVE")
    CampaignStatus status,

    @Schema(description = "Fecha de inicio", example = "2024-06-01")
    LocalDate startDate,

    @Schema(description = "Fecha de fin", example = "2024-07-01")
    LocalDate endDate,

    @Schema(description = "Fecha de creación", example = "2024-05-31T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "Fecha de última actualización", example = "2024-05-31T12:00:00")
    LocalDateTime updatedAt
) {} 