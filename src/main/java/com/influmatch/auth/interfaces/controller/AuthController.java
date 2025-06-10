package com.influmatch.auth.interfaces.controller;

import com.influmatch.auth.application.dto.AuthResponse;
import com.influmatch.auth.application.dto.LoginRequest;
import com.influmatch.auth.application.dto.RefreshTokenRequest;
import com.influmatch.auth.application.dto.RegisterRequest;
import com.influmatch.auth.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "APIs para registro, inicio de sesión y gestión de tokens")
public class AuthController {
    private final AuthService authService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @PostMapping("/register")
    @Operation(
        summary = "Registrar un nuevo usuario",
        description = "Registra un nuevo usuario con email, contraseña y rol (BRAND o INFLUENCER). Devuelve tokens JWT al registrarse exitosamente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (formato de email, campos faltantes) o email ya registrado",
            content = @Content
        )
    })
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Datos de registro", required = true)
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Inicia sesión con email y contraseña para recibir tokens JWT y estado de completitud del perfil"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content
        )
    })
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Credenciales de inicio de sesión", required = true)
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "Refrescar token de acceso",
        description = "Usa el token de refresco para obtener un nuevo par de tokens de acceso y refresco"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tokens renovados exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token de refresco inválido o expirado",
            content = @Content
        )
    })
    public ResponseEntity<AuthResponse> refresh(
            @Parameter(description = "Token de refresco", required = true)
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(
        summary = "Cerrar sesión",
        description = "Invalida la sesión actual. Requiere un token JWT válido en el header Authorization.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sesión cerrada exitosamente",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Se requiere un token JWT válido",
            content = @Content
        )
    })
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }
} 