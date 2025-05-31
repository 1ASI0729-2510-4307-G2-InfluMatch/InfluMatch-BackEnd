package com.influmatch.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

    /**  “/v3/api-docs” global  */
    @Bean
    Info apiInfo() {
        return new Info()
                .title("InfluMatch API")
                .version("v1")
                .description("""
                      Endpoints para identity-access, profiles,
                      collaboration, messaging, ratings y notifications.
                      """);
    }

    /**  Grupo opcional: solo Collaboration */
    @Bean
    GroupedOpenApi collaborationGroup() {
        return GroupedOpenApi.builder()
                .group("collaboration")
                .packagesToScan("com.influmatch.collaboration")
                .build();
    }
}
