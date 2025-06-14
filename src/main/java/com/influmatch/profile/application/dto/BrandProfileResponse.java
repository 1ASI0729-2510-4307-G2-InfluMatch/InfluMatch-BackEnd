package com.influmatch.profile.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Schema(description = "Datos de un perfil de marca")
public class BrandProfileResponse {
    @Schema(description = "ID del perfil")
    private Long id;

    @Schema(description = "Nombre de la marca")
    private String name;

    @Schema(description = "Sector o industria")
    private String sector;

    @Schema(description = "Código de país ISO 3166-1 alpha-2")
    private String country;

    @Schema(description = "Descripción detallada")
    private String description;

    @Schema(description = "Logo en base64")
    private String logo;

    @Schema(description = "Foto de perfil en base64")
    private String profilePhoto;

    @Schema(description = "URL del sitio web")
    private String websiteUrl;

    @Schema(description = "Ubicación")
    private String location;

    @Schema(description = "Lista de enlaces adicionales")
    @Builder.Default
    private List<LinkDto> links = new ArrayList<>();

    @Schema(description = "Lista de archivos adjuntos")
    @Builder.Default
    private List<AttachmentDto> attachments = new ArrayList<>();

    @Schema(description = "Fecha de creación")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
} 