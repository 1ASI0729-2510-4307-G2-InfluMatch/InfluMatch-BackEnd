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

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "Perfiles", description = "API para gestionar perfiles de marcas e influencers")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    private final ProfileService profileService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ProfileException.class)
    public ResponseEntity<String> handleProfileException(ProfileException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler(ProfileAlreadyExistsException.class)
    public ResponseEntity<String> handleProfileAlreadyExistsException(ProfileAlreadyExistsException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @Operation(
        summary = "Crear perfil de marca",
        description = "Crea un nuevo perfil para una marca. Requiere rol BRAND y no tener un perfil existente."
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
    @PostMapping(value = "/brand", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createBrandProfile(
            @Parameter(description = "Logo de la marca (opcional)") @RequestPart(required = false) MultipartFile logo,
            @Parameter(description = "Foto de perfil (opcional)") @RequestPart(required = false) MultipartFile profilePhoto,
            @Parameter(description = "Datos del perfil", required = true) @RequestPart @Valid CreateBrandProfileRequest request) {
        try {
            BrandProfileResponse response = profileService.createBrandProfile(request, logo, profilePhoto);
            return ResponseEntity.ok(response);
        } catch (ProfileAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (ProfileException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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