// src/main/java/com/influmatch/identityaccess/api/AuthController.java
package com.influmatch.identityaccess.api;

import com.influmatch.identityaccess.application.AuthService;
import com.influmatch.identityaccess.application.exceptions.InvalidCredentialsException;
import com.influmatch.identityaccess.domain.model.RoleEnum;
import com.influmatch.identityaccess.domain.model.User;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;

@Tag(name = "Autenticación", description = "API para registro, login y logout de usuarios")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea una nueva cuenta de usuario con el rol especificado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RegisterResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o email ya registrado",
            content = @Content
        )
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest req) {
        User registeredUser = auth.register(
                req.email(),
                req.password(),
                RoleEnum.valueOf(req.role().name())
        );

        String token = auth.generateToken(registeredUser);

        var body = new RegisterResponse(
            registeredUser.getId(),
            registeredUser.getEmail(),
            registeredUser.getRole().name(),
            token,
            "Registro exitoso!"
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica al usuario y retorna un token JWT"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = LoginResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Usuario no existe",
                        value = "{\"error\":\"email_not_found\",\"message\":\"El usuario no existe\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Contraseña incorrecta",
                        value = "{\"error\":\"invalid_password\",\"message\":\"Contraseña incorrecta\"}"
                    )
                }
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            User user = auth.login(req.email(), req.password());
            String token = auth.generateToken(user);

            var body = new LoginResponse(
                user.getId(),
                user.getEmail(), 
                user.getRole().name(),
                token,
                "Login exitoso!"
            );

            return ResponseEntity.ok(body);
        } catch (InvalidCredentialsException e) {
            String error = e.getMessage();
            String message = switch (error) {
                case "email_not_found" -> "El usuario no existe";
                case "invalid_password" -> "Contraseña incorrecta";
                default -> "Credenciales inválidas";
            };
            
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(error, message));
        }
    }

    @Operation(
        summary = "Cerrar sesión",
        description = "Cierra la sesión del usuario actual e invalida su token JWT. " +
                     "Requiere enviar el token JWT en el header 'Authorization' con el prefijo 'Bearer '"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sesión cerrada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(example = "{\"message\": \"logout_success\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token inválido o expirado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(example = "{\"error\": \"invalid_token\"}")
            )
        )
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @Parameter(description = "Token JWT con prefijo Bearer", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        // Extrae el token del header (elimina el prefijo "Bearer ")
        String token = authHeader.substring(7);
        
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "error", "unauthorized",
                "message", "Usuario no autenticado"
            ));
        }

        try {
            Long userId = Long.parseLong(authentication.getName());
            auth.logout(token, userId);
            return ResponseEntity.ok(Map.of(
                "message", "logout_success"
            ));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "error", "invalid_user_id",
                "message", "ID de usuario inválido"
            ));
        }
    }
}
