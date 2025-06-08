package com.influmatch.profiles.api.dto;

import com.influmatch.profiles.domain.model.PlatformEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Datos de red social")
public record SocialLinkRequest(
    @Schema(
        description = "Plataforma",
        example = "IG",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "platform_required")
    PlatformEnum platform,

    @Schema(
        description = "URL del perfil",
        example = "https://instagram.com/johndoe",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "url_required")
    @Pattern(regexp = "^(https?://)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$",
             message = "url_invalid")
    String url
) {} 