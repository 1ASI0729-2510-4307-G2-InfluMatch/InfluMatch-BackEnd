package com.influmatch.collaboration.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos resumidos de una colaboración para el listado")
public class CollaborationListDto {
    @Schema(description = "ID de la colaboración")
    private Long id;

    @Schema(description = "ID del usuario que envió la solicitud")
    private Long initiatorId;

    @Schema(description = "ID del usuario que recibió la solicitud")
    private Long counterpartId;

    @Schema(description = "Rol del iniciador", example = "BRAND")
    private String initiatorRole;

    @Schema(description = "Estado actual", example = "PENDING")
    private String status;

    @Schema(description = "Nombre de la contraparte")
    private String counterpartName;

    @Schema(description = "Foto de la contraparte en base64")
    private String counterpartPhotoUrl;

    @Schema(description = "Mensaje de la colaboración")
    private String message;

    @Schema(description = "Tipo de acción", example = "REEL_IG")
    private String actionType;

    @Schema(description = "Fecha de creación")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
} 