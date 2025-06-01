package com.influmatch.collaboration.api;

import com.influmatch.collaboration.domain.model.CampaignStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Datos para actualizar una campaña")
public record UpdateCampaignRequest(
    @Schema(description = "Nuevo título de la campaña", example = "Campaña Verano 2024")
    @Size(max = 100, message = "title_too_long")
    String title,

    @Schema(description = "Nuevo brief/descripción", example = "Promoción de nueva línea de productos...")
    @Size(max = 2000, message = "brief_too_long")
    String brief,

    @Schema(
        description = "Nuevo estado de la campaña", 
        example = "ACTIVE",
        allowableValues = {"DRAFT", "ACTIVE", "PAUSED", "FINISHED", "CANCELED"}
    )
    CampaignStatus status,

    @Schema(description = "Nueva fecha de inicio", example = "2024-06-01")
    @Future(message = "start_date_must_be_future")
    LocalDate startDate,

    @Schema(description = "Nueva fecha de fin", example = "2024-07-01")
    @Future(message = "end_date_must_be_future")
    LocalDate endDate
) {} 