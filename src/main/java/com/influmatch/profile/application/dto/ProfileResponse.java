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
@Schema(description = "Datos de un perfil (marca o influencer)")
public class ProfileResponse {
    @Schema(description = "ID del perfil")
    private Long id;

    @Schema(description = "Nombre del perfil")
    private String name;

    @Schema(description = "Sector o industria (solo para marcas)")
    private String sector;

    @Schema(description = "Lista de nichos o categorías (solo para influencers)")
    @Builder.Default
    private List<String> niches = new ArrayList<>();

    @Schema(description = "Biografía (solo para influencers)")
    private String bio;

    @Schema(description = "Código de país ISO 3166-1 alpha-2")
    private String country;

    @Schema(description = "Descripción detallada (solo para marcas)")
    private String description;

    @Schema(description = "Logo en base64 (solo para marcas)")
    private String logo;

    @Schema(description = "Foto principal en base64 (solo para influencers)")
    private String photo;

    @Schema(description = "Foto de perfil en base64")
    private String profilePhoto;

    @Schema(description = "URL del sitio web (solo para marcas)")
    private String websiteUrl;

    @Schema(description = "Número de seguidores (solo para influencers)")
    private Integer followers;

    @Schema(description = "Lista de enlaces a redes sociales (solo para influencers)")
    @Builder.Default
    private List<SocialLinkDto> socialLinks = new ArrayList<>();

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