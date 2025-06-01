package com.influmatch.profiles.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Datos para crear/actualizar perfil de marca")
public record BrandRequest(
    @Schema(
        description = "Nombre de la empresa",
        example = "Nike",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "company_name_required")
    @Size(min = 2, max = 50, message = "company_name_length")
    String companyName,

    @Schema(
        description = "Descripción",
        example = "Empresa líder en artículos deportivos"
    )
    @Size(max = 255, message = "description_length")
    String description,

    @Schema(
        description = "Industria",
        example = "Deportes"
    )
    @Size(max = 50, message = "industry_length")
    String industry,

    @Schema(
        description = "URL del sitio web",
        example = "https://www.nike.com"
    )
    @Pattern(regexp = "^(https?://)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$",
             message = "website_url_invalid")
    String websiteUrl,

    @Schema(
        description = "URL del logo",
        example = "https://example.com/logo.png"
    )
    @Pattern(regexp = "^(https?://)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$",
             message = "logo_url_invalid")
    String logoUrl
) {} 