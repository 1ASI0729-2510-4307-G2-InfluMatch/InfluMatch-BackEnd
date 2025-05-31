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
            /* ───── CSRF ───── */
            .csrf(csrf -> csrf.disable())       // ➊ desactívalo o cámbialo a tu gusto

            /* ───── AUTORIZACIONES ───── */
            .authorizeHttpRequests(auth -> auth
                /* ➋ Swagger & OpenAPI  ─ rutas públicas  */
                .requestMatchers(
                        //  ruta “vieja”  (por si alguien la conoce)
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",

                        //  rutas “nuevas” según application.yml
                        "/api/docs/**",
                        "/api/swagger.html",
                        "/api/swagger/**"
                ).permitAll()

                /* ➌ Actuator - health (opcional) */
                .requestMatchers("/actuator/health").permitAll()

                /* ➍ Todo lo demás exige autenticación */
                .anyRequest().authenticated()
            );

        /*  Aquí decides tu mecanismo de auth:  */
        // http.httpBasic(Customizer.withDefaults());   // BASIC
        // http.oauth2ResourceServer(oauth2 -> …);     // JWT
        // http.formLogin(Customizer.withDefaults());  // sesión, etc.

        return http.build();
    }
}
