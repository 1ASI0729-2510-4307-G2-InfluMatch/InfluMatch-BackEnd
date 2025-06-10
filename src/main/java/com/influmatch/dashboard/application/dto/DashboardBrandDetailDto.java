package com.influmatch.dashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.influmatch.shared.application.dto.AttachmentDto;
import com.influmatch.shared.application.dto.LinkDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos detallados de una marca")
public class DashboardBrandDetailDto {
    @Schema(description = "ID de la marca")
    private Long id;

    @Schema(description = "Nombre de la marca")
    private String name;

    @Schema(description = "Sector o industria de la marca")
    private String sector;

    @Schema(description = "Descripción de la marca")
    private String description;

    @Schema(description = "Logo de la marca en base64")
    private String logo;

    @Schema(description = "Foto de perfil en base64")
    private String profilePhoto;

    @Schema(description = "Código de país ISO 3166-1 alpha-2")
    private String country;

    @Schema(description = "URL del sitio web")
    private String websiteUrl;

    @Schema(description = "Ubicación")
    private String location;

    @Schema(description = "Enlaces adicionales")
    @Builder.Default
    private List<LinkDto> links = new ArrayList<>();

    @Schema(description = "Archivos adjuntos")
    @Builder.Default
    private List<AttachmentDto> attachments = new ArrayList<>();

    @Schema(description = "Calificación promedio de colaboraciones")
    private Float rating;

    @Schema(description = "Fecha de creación")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
} 