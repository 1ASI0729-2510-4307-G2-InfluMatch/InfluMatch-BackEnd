package com.influmatch.dashboard.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos resumidos de una marca para el listado")
public class DashboardBrandListDto {
    @Schema(description = "ID del usuario de la marca")
    private Long userId;

    @Schema(description = "Nombre comercial de la marca")
    private String tradeName;

    @Schema(description = "URL del logo de la marca en base64")
    private String logoUrl;

    @Schema(description = "País de la marca")
    private String country;

    @Schema(description = "Sector o industria de la marca")
    private String sector;
} 