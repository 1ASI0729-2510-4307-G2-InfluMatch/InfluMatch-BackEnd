package com.influmatch.security;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;

@Service
public class TokenBlacklistService {
    
    // Almacena tokens invalidados y su tiempo de expiración
    private final ConcurrentHashMap<String, Instant> blacklist = new ConcurrentHashMap<>();

    /**
     * Agrega un token a la lista negra
     * @param token El token JWT a invalidar
     * @param expiresAt Cuando expira el token
     */
    public void blacklist(String token, Instant expiresAt) {
        blacklist.put(token, expiresAt);
        removeExpiredTokens();
    }

    /**
     * Verifica si un token está en la lista negra
     */
    public boolean isBlacklisted(String token) {
        removeExpiredTokens();
        return blacklist.containsKey(token);
    }

    /**
     * Limpia tokens expirados de la lista negra
     */
    private void removeExpiredTokens() {
        Instant now = Instant.now();
        blacklist.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }
} 