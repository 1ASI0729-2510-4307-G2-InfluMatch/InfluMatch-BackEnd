package com.influmatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        /* rutas “viejas” por compatibilidad */
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",

                        /* rutas configuradas en application.yml */
                        "/api/docs/**",
                        "/api/swagger",        //   <─ NUEVA
                        "/api/swagger/",       //   <─ NUEVA (con ‘/’)
                        "/api/swagger.html",
                        "/api/swagger/**"
                ).permitAll()
                .anyRequest().permitAll()     // ← solo mientras no tengas auth
            );

        return http.build();
    }
}
