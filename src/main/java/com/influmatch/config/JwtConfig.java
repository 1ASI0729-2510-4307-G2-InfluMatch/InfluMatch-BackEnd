// src/main/java/com/influmatch/config/JwtConfig.java
package com.influmatch.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${security.jwt.secret}")
    private String base64Secret;   // viene del yml / env

    @Value("${security.jwt.exp-seconds:3600}")
    private long expSeconds;

    /** Clave HS256 segura (>=256 bits) */
    @Bean
    public SecretKey jwtKey() {
        byte[] bytes = Decoders.BASE64.decode(base64Secret);
        if (bytes.length < 32) {          // 32 bytes = 256 bits
            throw new IllegalStateException(
                "JWT secret must be at least 256 bits (32 bytes)."
            );
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    /** Tiempo de vida del token (lo inyectarán donde lo necesiten) */
    @Bean
    public Long jwtExpSeconds() {
        return expSeconds;
    }
}
