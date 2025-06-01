package com.influmatch.media.api;

import com.influmatch.media.application.MediaService;
import com.influmatch.media.application.StorageService;
import com.influmatch.media.domain.model.MediaFile;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;

@Tag(name = "Media", description = "API para gestión de activos multimedia")
@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MediaController {

    private final MediaService mediaService;
    private final StorageService storage;

    @Operation(
        summary = "Subir nuevo activo multimedia",
        description = "Sube un nuevo archivo (video, imagen o documento) y crea su registro"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Activo creado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = MediaResponse.class)
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
                        name = "Archivo requerido",
                        value = "{\"error\":\"file_required\",\"message\":\"Debe proporcionar un archivo\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Tipo no soportado",
                        value = "{\"error\":\"unsupported_media_type\",\"message\":\"Tipo de archivo no soportado\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Archivo muy grande",
                        value = "{\"error\":\"file_too_large\",\"message\":\"El archivo excede el tamaño máximo permitido\"}"
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaResponse> uploadMedia(
            @Valid @ModelAttribute CreateMediaRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        MediaFile asset = mediaService.createMedia(request, userId);
        
        return ResponseEntity.ok(toResponse(asset));
    }

    @Operation(
        summary = "Obtener activo multimedia",
        description = "Recupera los detalles de un activo multimedia por su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Activo encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = MediaResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Activo no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"media_not_found\",\"message\":\"El activo multimedia no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MediaResponse> getMedia(
            @Parameter(description = "ID del activo multimedia", example = "1")
            @PathVariable Long id) {
        
        MediaFile asset = mediaService.getMediaById(id);
        return ResponseEntity.ok(toResponse(asset));
    }

    @Operation(
        summary = "Actualizar activo multimedia",
        description = "Actualiza los campos editables de un activo multimedia existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Activo actualizado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = MediaResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Activo no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"media_not_found\",\"message\":\"El activo multimedia no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para modificar este activo",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"not_authorized\",\"message\":\"No tiene permiso para modificar este activo\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<MediaResponse> updateMedia(
            @Parameter(description = "ID del activo multimedia", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateMediaRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        MediaFile asset = mediaService.updateMedia(id, request, userId);
        return ResponseEntity.ok(toResponse(asset));
    }

    @Operation(
        summary = "Eliminar activo multimedia",
        description = "Elimina un activo multimedia y su archivo asociado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Activo eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Activo no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"media_not_found\",\"message\":\"El activo multimedia no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para eliminar este activo",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"not_authorized\",\"message\":\"No tiene permiso para eliminar este activo\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(
            @Parameter(description = "ID del activo multimedia", example = "1")
            @PathVariable Long id,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        mediaService.deleteMedia(id, userId);
        return ResponseEntity.noContent().build();
    }

    private MediaResponse toResponse(MediaFile asset) {
        return new MediaResponse(
            asset.getId(),
            asset.getFilename(),
            storage.getUrl(asset.getFilename(), asset.getContent(), asset.getContentType()),
            asset.getContentType(),
            asset.getSize(),
            asset.getType(),
            asset.getTitle(),
            asset.getDescription(),
            asset.getWidth(),
            asset.getHeight(),
            asset.getDuration(),
            asset.getUploadedBy(),
            asset.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            asset.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
} 