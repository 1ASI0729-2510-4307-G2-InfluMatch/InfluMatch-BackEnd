package com.influmatch.profiles.api;

import com.influmatch.profiles.api.dto.InfluencerRequest;
import com.influmatch.profiles.api.dto.InfluencerResponse;
import com.influmatch.profiles.application.InfluencerService;
import com.influmatch.profiles.domain.model.InfluencerProfile;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Influencers", description = "API para gestionar perfiles de influencers")
@RestController
@RequestMapping("/api/influencers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class InfluencerController {

    private final InfluencerService service;

    @Operation(
        summary = "Listar influencers",
        description = "Obtiene la lista de todos los perfiles de influencers"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = InfluencerResponse.class)
            )
        )
    })
    @GetMapping
    @PreAuthorize("hasRole('BRAND')")
    public ResponseEntity<List<InfluencerResponse>> findAll() {
        List<InfluencerResponse> responses = service.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "Obtener influencer",
        description = "Obtiene un perfil de influencer por su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Perfil encontrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = InfluencerResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Perfil no encontrado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BRAND', 'INFLUENCER')")
    public ResponseEntity<InfluencerResponse> findById(@PathVariable Long id) {
        InfluencerProfile profile = service.findById(id);
        return ResponseEntity.ok(toResponse(profile));
    }

    @Operation(
        summary = "Crear influencer",
        description = "Crea un nuevo perfil de influencer"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Perfil creado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = InfluencerResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para crear perfil con este userId",
            content = @Content
        )
    })
    @PostMapping
    @PreAuthorize("hasRole('INFLUENCER') and #userId == authentication.principal.id")
    public ResponseEntity<InfluencerResponse> create(
            @RequestParam Long userId,
            @Valid @RequestBody InfluencerRequest request) {
        
        InfluencerProfile profile = service.create(
            userId,
            request.displayName(),
            request.bio(),
            request.category(),
            request.country(),
            request.followersCount()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(toResponse(profile));
    }

    @Operation(
        summary = "Actualizar influencer",
        description = "Actualiza un perfil de influencer existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Perfil actualizado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = InfluencerResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Perfil no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para modificar este perfil",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INFLUENCER') and @influencerSecurity.isProfileOwner(#id)")
    public ResponseEntity<InfluencerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InfluencerRequest request) {

        InfluencerProfile profile = service.update(
            id,
            request.displayName(),
            request.bio(),
            request.category(),
            request.country(),
            request.followersCount()
        );

        return ResponseEntity.ok(toResponse(profile));
    }

    @Operation(
        summary = "Eliminar influencer",
        description = "Elimina un perfil de influencer"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Perfil eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Perfil no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para eliminar este perfil",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INFLUENCER') and @influencerSecurity.isProfileOwner(#id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private InfluencerResponse toResponse(InfluencerProfile profile) {
        return new InfluencerResponse(
            profile.getId(),
            profile.getUser().getId(),
            profile.getDisplayName(),
            profile.getBio(),
            profile.getCategory(),
            profile.getCountry(),
            profile.getFollowersCount(),
            "Operación exitosa"
        );
    }
} 