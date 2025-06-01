package com.influmatch.collaboration.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Datos para crear una nueva campaña")
public record CreateCampaignRequest(
    @Schema(description = "ID del influencer seleccionado", example = "123")
    @NotNull(message = "influencer_required")
    Long influencerId,

    @Schema(description = "Título de la campaña", example = "Campaña Verano 2024")
    @NotBlank(message = "title_required")
    @Size(max = 100, message = "title_too_long")
    String title,

    @Schema(description = "Brief/descripción de la campaña", example = "Promoción de nueva línea de productos...")
    @Size(max = 2000, message = "brief_too_long")
    String brief,

    @Schema(description = "Fecha de inicio", example = "2024-06-01")
    @NotNull(message = "start_date_required")
    @Future(message = "start_date_must_be_future")
    LocalDate startDate,

    @Schema(description = "Fecha de fin", example = "2024-07-01")
    @NotNull(message = "end_date_required")
    @Future(message = "end_date_must_be_future")
    LocalDate endDate
) {} 