package com.influmatch.config;

import com.influmatch.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración central de Spring-Security.
 *
 * – Stateless: sin sesiones servidor.  
 * – Deshabilita CSRF/BASIC (solo API).  
 * – Añade el filtro JWT antes de UsernamePasswordAuthenticationFilter.  
 * – Expone como públicas las rutas de login, register y documentación.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;

    @Bean
    SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http
            // ➊ API sin estado
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ➋ Fuera CSRF + BASIC
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            // ➌ Autorización
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        // Endpoints públicos
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/auth/verify-email",
                        // Swagger / OpenAPI
                        "/v3/api-docs/**",
                        "/swagger-ui.html", "/swagger-ui/**",
                        "/api/docs/**", "/api/swagger/**"
                ).permitAll()
                // Cualquier otro requiere JWT válido
                .anyRequest().authenticated()
            )

            // ➍ Filtro JWT
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
