package com.influmatch.profile.infrastructure.web;

import com.influmatch.profile.application.dto.*;
import com.influmatch.profile.application.service.ProfileService;
import com.influmatch.profile.domain.exception.ProfileException;
import com.influmatch.profile.domain.exception.ProfileAlreadyExistsException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "Perfiles", description = "API para gestionar perfiles de marcas e influencers")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    private final ProfileService profileService;

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Tipo de contenido no soportado");
        error.put("mensaje", "El endpoint no acepta el tipo de contenido enviado. Por favor, use el endpoint correcto:");
        error.put("para_json", "Use /api/profiles/brand/json para enviar datos en formato JSON");
        error.put("para_multipart", "Use /api/profiles/brand para enviar datos en formato multipart/form-data");
        error.put("detalles", ex.getMessage());
        
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }

    @ExceptionHandler(ProfileException.class)
    public ResponseEntity<Map<String, String>> handleProfileException(ProfileException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error de perfil");
        error.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(ProfileAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleProfileAlreadyExists(ProfileAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Perfil ya existe");
        error.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Datos inválidos");
        error.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Operation(
        summary = "Crear perfil de marca",
        description = "Crea un nuevo perfil para una marca usando JSON. Requiere rol BRAND y no tener un perfil existente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Perfil creado exitosamente",
            content = @Content(schema = @Schema(implementation = BrandProfileResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o faltantes",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Rol de usuario incorrecto",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "El usuario ya tiene un perfil",
            content = @Content
        )
    })
    @PostMapping(value = "/brand", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBrandProfile(
            @Parameter(description = "Datos del perfil", required = true) @RequestBody @Valid CreateBrandProfileRequest request) {
        try {
            BrandProfileResponse response = profileService.createBrandProfile(request, null, null);
            return ResponseEntity.ok(response);
        } catch (ProfileAlreadyExistsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Perfil ya existe");
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(409).body(error);
        } catch (ProfileException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error de permisos");
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(403).body(error);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Datos inválidos");
            error.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @Operation(
        summary = "Crear perfil de influencer",
        description = "Crea un nuevo perfil para un influencer usando JSON con datos base64. Requiere rol INFLUENCER y no tener un perfil existente."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Perfil creado exitosamente",
        content = @Content(schema = @Schema(implementation = InfluencerProfileResponse.class))
    )
    @PostMapping(value = "/influencer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public InfluencerProfileResponse createInfluencerProfile(
            @Parameter(description = "Datos del perfil", required = true) @RequestBody @Valid CreateInfluencerProfileRequest request) {
        return profileService.createInfluencerProfile(request, null, null, null);
    }

    @Operation(
        summary = "Obtener perfil de marca",
        description = "Obtiene el perfil de la marca autenticada. Requiere rol BRAND y tener un perfil existente."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Perfil encontrado",
        content = @Content(schema = @Schema(implementation = BrandProfileResponse.class))
    )
    @GetMapping("/brand/me")
    public BrandProfileResponse getBrandProfile() {
        return profileService.getBrandProfile();
    }

    @Operation(
        summary = "Obtener perfil de influencer",
        description = "Obtiene el perfil del influencer autenticado. Requiere rol INFLUENCER y tener un perfil existente."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Perfil encontrado",
        content = @Content(schema = @Schema(implementation = InfluencerProfileResponse.class))
    )
    @GetMapping("/influencer/me")
    public InfluencerProfileResponse getInfluencerProfile() {
        return profileService.getInfluencerProfile();
    }

    @Operation(
        summary = "Actualizar perfil de marca",
        description = "Actualiza el perfil de la marca autenticada. Requiere rol BRAND y tener un perfil existente."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Perfil actualizado exitosamente",
        content = @Content(schema = @Schema(implementation = BrandProfileResponse.class))
    )
    @PutMapping(value = "/brand/me", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BrandProfileResponse updateBrandProfile(
            @Parameter(description = "Logo de la marca (opcional)") @RequestPart(required = false) MultipartFile logo,
            @Parameter(description = "Foto de perfil (opcional)") @RequestPart(required = false) MultipartFile profilePhoto,
            @Parameter(description = "Datos del perfil", required = true) @RequestPart @Valid CreateBrandProfileRequest request) {
        return profileService.updateBrandProfile(request, logo, profilePhoto);
    }

    @Operation(
        summary = "Actualizar perfil de influencer",
        description = "Actualiza el perfil del influencer autenticado. Requiere rol INFLUENCER y tener un perfil existente."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Perfil actualizado exitosamente",
        content = @Content(schema = @Schema(implementation = InfluencerProfileResponse.class))
    )
    @PutMapping(value = "/influencer/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public InfluencerProfileResponse updateInfluencerProfile(
            @Parameter(description = "Datos del perfil", required = true) @RequestBody @Valid CreateInfluencerProfileRequest request) {
        return profileService.updateInfluencerProfile(request, null, null, null);
    }
} 