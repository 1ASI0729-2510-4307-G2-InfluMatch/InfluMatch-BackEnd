package com.influmatch.ratings.api;

import com.influmatch.ratings.application.RatingService;
import com.influmatch.ratings.domain.model.Rating;
import com.influmatch.shared.api.ErrorResponse;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ratings", description = "API para gestión de valoraciones")
@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RatingController {

    private final RatingService ratingService;

    @Operation(
        summary = "Listar valoraciones",
        description = "Recupera todas las valoraciones con soporte para filtros y paginación"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de valoraciones recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RatingResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para aplicar los filtros especificados",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_filter\",\"message\":\"No puede filtrar por valoraciones de otros usuarios\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<Page<RatingResponse>> getRatings(
            @Parameter(description = "ID del autor (opcional)", example = "1")
            @RequestParam(required = false) Long writerId,
            @Parameter(description = "ID del destinatario (opcional)", example = "1")
            @RequestParam(required = false) Long targetId,
            @Parameter(description = "ID de la campaña (opcional)", example = "1")
            @RequestParam(required = false) Long campaignId,
            @ParameterObject Pageable pageable,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<Rating> ratings = ratingService.getRatings(
            writerId, targetId, campaignId, userId, isAdmin, pageable
        );
        
        return ResponseEntity.ok(ratings.map(this::toResponse));
    }

    @Operation(
        summary = "Obtener valoración",
        description = "Recupera los detalles de una valoración específica"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Valoración recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RatingResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para ver esta valoración",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_rating\",\"message\":\"No tiene permiso para ver esta valoración\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Valoración no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"rating_not_found\",\"message\":\"La valoración no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping("/{ratingId}")
    public ResponseEntity<RatingResponse> getRating(
            @Parameter(description = "ID de la valoración", example = "1")
            @PathVariable Long ratingId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Rating rating = ratingService.getRating(ratingId, userId, isAdmin);
        return ResponseEntity.ok(toResponse(rating));
    }

    @Operation(
        summary = "Crear valoración",
        description = "Crea una nueva valoración"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Valoración creada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RatingResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Score inválido",
                        value = "{\"error\":\"score_invalid\",\"message\":\"La puntuación debe estar entre 1 y 5\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Target inválido",
                        value = "{\"error\":\"invalid_target\",\"message\":\"El ID del destinatario es inválido\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Campaña inválida",
                        value = "{\"error\":\"invalid_campaign_participants\",\"message\":\"Ambos usuarios deben haber participado en la campaña\"}"
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Campaña no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"campaign_not_found\",\"message\":\"La campaña no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<RatingResponse> createRating(
            @Valid @RequestBody CreateRatingRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Rating rating = ratingService.createRating(request, userId);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(toResponse(rating));
    }

    @Operation(
        summary = "Actualizar valoración",
        description = "Modifica una valoración existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Valoración actualizada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RatingResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"score_invalid\",\"message\":\"La puntuación debe estar entre 1 y 5\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para modificar esta valoración",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_rating\",\"message\":\"No tiene permiso para modificar esta valoración\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Valoración no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"rating_not_found\",\"message\":\"La valoración no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PutMapping("/{ratingId}")
    public ResponseEntity<RatingResponse> updateRating(
            @Parameter(description = "ID de la valoración", example = "1")
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Rating rating = ratingService.updateRating(ratingId, request, userId, isAdmin);
        return ResponseEntity.ok(toResponse(rating));
    }

    @Operation(
        summary = "Eliminar valoración",
        description = "Elimina una valoración existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Valoración eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para eliminar esta valoración",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_rating\",\"message\":\"No tiene permiso para eliminar esta valoración\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Valoración no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"rating_not_found\",\"message\":\"La valoración no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(
            @Parameter(description = "ID de la valoración", example = "1")
            @PathVariable Long ratingId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        ratingService.deleteRating(ratingId, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    private RatingResponse toResponse(Rating rating) {
        return new RatingResponse(
            rating.getId(),
            rating.getWriterId(),
            rating.getTargetId(),
            rating.getCampaign() != null ? rating.getCampaign().getId() : null,
            rating.getScore(),
            rating.getComment(),
            rating.getCreatedAt(),
            rating.getUpdatedAt()
        );
    }
} 