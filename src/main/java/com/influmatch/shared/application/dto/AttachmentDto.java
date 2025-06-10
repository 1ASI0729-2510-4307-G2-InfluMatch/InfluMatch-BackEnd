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
@Schema(description = "Archivo adjunto")
public class AttachmentDto {
    @Schema(description = "Título del archivo")
    private String title;

    @Schema(description = "Descripción del archivo")
    private String description;

    @Schema(description = "Tipo de medio (PHOTO, VIDEO, DOCUMENT)")
    private String mediaType;

    @Schema(description = "Contenido del archivo en base64")
    private String data;
} 