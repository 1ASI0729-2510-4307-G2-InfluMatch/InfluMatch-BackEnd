package com.influmatch.collaboration.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Hito o evento dentro de una colaboración")
public class MilestoneDto {
    @Schema(description = "Título del hito", example = "Reel")
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Fecha del hito", example = "2025-07-12")
    @NotNull(message = "Date is required")
    private String date;

    @Schema(description = "Descripción detallada del hito", example = "Feed post")
    private String description;

    @Schema(description = "Ubicación o enlace de reunión", example = "https://meet.link/abc")
    private String location;

    @Schema(description = "Entregables esperados", example = "1 reel + 3 stories")
    private String deliverables;
} 