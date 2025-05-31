// src/main/java/com/influmatch/identityaccess/api/AuthController.java
package com.influmatch.identityaccess.api;

import com.influmatch.identityaccess.application.AuthService;
import com.influmatch.identityaccess.domain.model.RoleEnum;
import com.influmatch.identityaccess.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "API para registro e inicio de sesión de usuarios")
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
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content
        )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
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
    }
}
