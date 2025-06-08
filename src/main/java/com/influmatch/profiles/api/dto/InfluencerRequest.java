package com.influmatch.profiles.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Schema(description = "Datos para crear/actualizar perfil de influencer")
public record InfluencerRequest(
    @Schema(
        description = "Nombre a mostrar",
        example = "John Doe"
    )
    @Size(min = 3, max = 50, message = "display_name_length")
    String displayName,

    @Schema(
        description = "Biografía",
        example = "Lifestyle influencer"
    )
    @Size(max = 255, message = "bio_length")
    String bio,

    @Schema(
        description = "Categoría",
        example = "LIFESTYLE"
    )
    @Size(max = 50, message = "category_length")
    String category,

    @Schema(
        description = "País",
        example = "España"
    )
    @Size(max = 50, message = "country_length")
    String country,

    @Schema(
        description = "Número de seguidores",
        example = "10000"
    )
    Long followersCount,

    @Schema(
        description = "Lista de redes sociales"
    )
    @Valid
    List<SocialLinkRequest> socialLinks,

    @Schema(
        description = "Lista de archivos multimedia"
    )
    @Valid
    List<MediaAssetRequest> mediaAssets,

    @Schema(
        description = "Imagen de perfil en base64",
        example = "data:image/jpeg;base64,/9j/4AAQSkZJRg..."
    )
    String profilePictureBase64
) {} 