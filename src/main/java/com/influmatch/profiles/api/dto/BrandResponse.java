package com.influmatch.profiles.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de perfil de marca")
public record BrandResponse(
    @Schema(description = "ID del perfil", example = "1")
    Long id,

    @Schema(description = "ID del usuario asociado", example = "1")
    Long userId,

    @Schema(description = "Nombre de la empresa", example = "Nike")
    String companyName,

    @Schema(description = "Descripción", example = "Empresa líder en artículos deportivos")
    String description,

    @Schema(description = "Industria", example = "Deportes")
    String industry,

    @Schema(description = "URL del sitio web", example = "https://www.nike.com")
    String websiteUrl,

    @Schema(description = "URL del logo", example = "https://example.com/logo.png")
    String logoUrl,

    @Schema(description = "Mensaje de la operación", example = "Perfil encontrado exitosamente")
    String message
) {} 