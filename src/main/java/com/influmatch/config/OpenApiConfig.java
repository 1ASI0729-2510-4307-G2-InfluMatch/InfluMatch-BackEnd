package com.influmatch.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "InfluMatch API",
        version = "1.0",
        description = "API para la plataforma de matching entre influencers y marcas",
        contact = @Contact(
            name = "InfluMatch Team",
            email = "support@influmatch.com"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Servidor local de desarrollo"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT token authentication",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    /**  Grupo opcional solo Collaboration  */
    @Bean
    public GroupedOpenApi collaborationGroup() {
        return GroupedOpenApi.builder()
                .group("collaboration")
                .packagesToScan("com.influmatch.collaboration")
                .build();
    }
}
