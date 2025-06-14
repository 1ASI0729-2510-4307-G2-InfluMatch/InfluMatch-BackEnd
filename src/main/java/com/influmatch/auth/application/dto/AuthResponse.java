package com.influmatch.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta de autenticación")
public class AuthResponse {
    @Schema(description = "Token de acceso JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Token de refresco JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Indica si el usuario tiene un perfil completo", example = "false")
    private boolean profileCompleted;

    @Schema(description = "ID del usuario autenticado", example = "123")
    private Long userId;

    @Schema(description = "Nombre del usuario", example = "John Doe", required = false)
    private String name;

    @Schema(description = "URL de la foto de perfil del usuario", example = "https://example.com/photos/user123.jpg", required = false)
    private String photoUrl;
} 