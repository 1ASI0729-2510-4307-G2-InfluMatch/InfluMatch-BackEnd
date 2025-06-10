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

    @Schema(description = "Rol del iniciador", example = "BRAND")
    private String initiatorRole;

    @Schema(description = "Estado actual", example = "PENDING")
    private String status;

    @Schema(description = "Nombre de la contraparte")
    private String counterpartName;

    @Schema(description = "Fecha de creación")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
} 