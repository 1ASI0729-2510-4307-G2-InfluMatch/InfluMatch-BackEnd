package com.influmatch.profile.application.dto;

import com.influmatch.profile.domain.model.valueobject.MediaType;
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
@Schema(description = "Datos de un archivo adjunto")
public class AttachmentDto {
    @Schema(description = "Título del archivo", example = "Portafolio 2024")
    @NotBlank(message = "Attachment title is required")
    private String title;

    @Schema(description = "Descripción del archivo", example = "Presentación de trabajos realizados")
    private String description;

    @Schema(description = "Tipo de archivo (PHOTO, VIDEO, DOCUMENT)")
    @NotNull(message = "Media type is required")
    private MediaType mediaType;

    @Schema(description = "Contenido del archivo en Base64")
    @NotBlank(message = "Media data is required")
    private String data;  // Base64 encoded data
} 