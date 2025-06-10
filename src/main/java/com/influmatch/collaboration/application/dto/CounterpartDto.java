package com.influmatch.collaboration.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información básica de la contraparte en una colaboración")
public class CounterpartDto {
    @Schema(description = "ID del usuario de la contraparte")
    private Long id;

    @Schema(description = "Nombre de la contraparte")
    private String name;

    @Schema(description = "URL de la foto de perfil en base64")
    private String photoUrl;
} 