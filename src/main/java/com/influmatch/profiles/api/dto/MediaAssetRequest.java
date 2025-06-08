package com.influmatch.profiles.api.dto;

import com.influmatch.profiles.domain.model.MediaTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos de archivo multimedia")
public record MediaAssetRequest(
    @Schema(
        description = "URL del archivo",
        example = "https://example.com/image.jpg",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "url_required")
    @Pattern(regexp = "^(https?://)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$",
             message = "url_invalid")
    String url,

    @Schema(
        description = "Tipo de archivo",
        example = "IMAGE",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "media_type_required")
    MediaTypeEnum mediaType,

    @Schema(
        description = "Título",
        example = "Mi foto"
    )
    @Size(max = 255, message = "title_length")
    String title,

    @Schema(
        description = "Descripción",
        example = "Descripción de la foto"
    )
    @Size(max = 255, message = "description_length")
    String description,

    @Schema(
        description = "Tamaño en bytes",
        example = "1024000"
    )
    Long sizeBytes,

    @Schema(
        description = "Metadatos adicionales en formato JSON",
        example = "{\"width\":800,\"height\":600}"
    )
    String metadata,

    @Schema(
        description = "Contenido del archivo en base64",
        example = "data:image/jpeg;base64,/9j/4AAQSkZJRg..."
    )
    String base64Content
) {} 