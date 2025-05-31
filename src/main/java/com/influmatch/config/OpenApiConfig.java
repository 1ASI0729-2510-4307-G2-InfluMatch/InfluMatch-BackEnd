package com.influmatch.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**  Metadatos globales  ─  /api/docs  */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(
            new Info()
                .title("InfluMatch API")
                .version("v1")
                .description("""
                     Endpoints para identity-access, profiles,
                     collaboration, messaging, ratings y notifications.
                     """)
        );
    }

    /**  Grupo opcional solo Collaboration  */
    @Bean
    public GroupedOpenApi collaborationGroup() {
        return GroupedOpenApi.builder()
                .group("collaboration")
                .packagesToScan("com.influmatch.collaboration")
                .build();
    }
}
