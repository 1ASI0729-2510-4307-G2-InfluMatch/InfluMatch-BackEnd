package com.influmatch.profile.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear o actualizar un perfil de marca")
public class CreateBrandProfileRequest {
    @Schema(description = "Nombre de la marca", example = "Nike")
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Sector o industria de la marca", example = "Deportes")
    @NotBlank(message = "Sector is required")
    private String sector;

    @Schema(description = "Código de país ISO 3166-1 alpha-2", example = "US")
    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country must be a valid ISO 3166-1 alpha-2 code")
    private String country;

    @Schema(description = "Descripción detallada de la marca")
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Schema(description = "Logo de la marca en Base64 (opcional si se usa multipart)")
    private String logo;  // Base64 or null if using multipart

    @Schema(description = "Foto de perfil en Base64 (opcional si se usa multipart)")
    private String profilePhoto;  // Base64 or null if using multipart

    @Schema(description = "URL del sitio web", example = "https://www.nike.com")
    @URL(message = "Website URL must be a valid URL")
    private String websiteUrl;

    @Schema(description = "Ubicación de la marca", example = "Portland, Oregon")
    private String location;

    @Schema(description = "Lista de enlaces relacionados")
    @Valid
    @Builder.Default
    private List<LinkDto> links = new ArrayList<>();

    @Schema(description = "Lista de archivos adjuntos")
    @Valid
    @Builder.Default
    private List<AttachmentDto> attachments = new ArrayList<>();
} 