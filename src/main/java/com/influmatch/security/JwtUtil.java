package com.influmatch.security;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expSeconds;

    public JwtUtil(SecretKey key, Long expSeconds) {
        this.key = key;
        this.expSeconds = expSeconds;
    }

    public String generate(Long userId, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                   .subject(String.valueOf(userId))
                   .claim("role", role)
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(now.plusSeconds(expSeconds)))
                   .signWith(key)          // HS256
                   .compact();
    }

    public Long validateAndGetUser(String token) {
        return Long.valueOf(
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject()
        );
    }
}
