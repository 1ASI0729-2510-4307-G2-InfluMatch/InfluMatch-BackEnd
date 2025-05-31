package com.influmatch.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    /**  Metadatos globales  ─  /api/docs  */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("InfluMatch API")
                        .version("1.0")
                        .description("API para conectar marcas con influencers")
                        .contact(new Contact()
                                .name("InfluMatch Team")
                                .email("support@influmatch.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo")
                ));
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
