package com.influmatch.profiles.api;

import com.influmatch.profiles.api.dto.BrandRequest;
import com.influmatch.profiles.api.dto.BrandResponse;
import com.influmatch.profiles.application.BrandService;
import com.influmatch.profiles.domain.model.BrandProfile;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Marcas", description = "API para gestionar perfiles de marcas")
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BrandController {

    private final BrandService service;

    @Operation(
        summary = "Listar marcas",
        description = "Obtiene la lista de todos los perfiles de marcas"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandResponse.class)
            )
        )
    })
    @GetMapping
    @PreAuthorize("hasRole('INFLUENCER')")
    public ResponseEntity<List<BrandResponse>> findAll() {
        List<BrandResponse> responses = service.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "Obtener marca",
        description = "Obtiene un perfil de marca por su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Perfil encontrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandResponse.class)
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
    public ResponseEntity<BrandResponse> findById(@PathVariable Long id) {
        BrandProfile profile = service.findById(id);
        return ResponseEntity.ok(toResponse(profile));
    }

    @Operation(
        summary = "Crear marca",
        description = "Crea un nuevo perfil de marca para el usuario autenticado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Perfil creado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado",
            content = @Content
        )
    })
    @PostMapping
    @PreAuthorize("hasRole('BRAND')")
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandRequest request) {
        // Obtener el ID del usuario del token JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        BrandProfile profile = service.create(
            userId,
            request.companyName(),
            request.description(),
            request.industry(),
            request.websiteUrl(),
            request.logoUrl()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(toResponse(profile));
    }

    @Operation(
        summary = "Actualizar marca",
        description = "Actualiza un perfil de marca existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Perfil actualizado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandResponse.class)
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
    @PreAuthorize("hasRole('BRAND') and @brandSecurity.isProfileOwner(#id)")
    public ResponseEntity<BrandResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest request) {

        BrandProfile profile = service.update(
            id,
            request.companyName(),
            request.description(),
            request.industry(),
            request.websiteUrl(),
            request.logoUrl()
        );

        return ResponseEntity.ok(toResponse(profile));
    }

    @Operation(
        summary = "Eliminar marca",
        description = "Elimina un perfil de marca"
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
    @PreAuthorize("hasRole('BRAND') and @brandSecurity.isProfileOwner(#id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private BrandResponse toResponse(BrandProfile profile) {
        return new BrandResponse(
            profile.getId(),
            profile.getUser().getId(),
            profile.getCompanyName(),
            profile.getDescription(),
            profile.getIndustry(),
            profile.getWebsiteUrl(),
            profile.getLogoUrl(),
            "Operación exitosa"
        );
    }
} 