package com.influmatch.shared.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Enlace genérico")
public class LinkDto {
    @Schema(description = "Título del enlace")
    private String title;

    @Schema(description = "URL del enlace")
    private String url;
} 