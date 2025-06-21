package com.influmatch.auth.application.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class TokenBlacklistService {
    
    // Conjunto thread-safe para almacenar tokens que han sido puestos en la lista negra
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    // Método para agregar un token a la lista negra
    public void blacklist(String token) {
        blacklistedTokens.add(token); // Añade el token al conjunto de tokens bloqueados
    }

    // Método para verificar si un token está en la lista negra
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token); // Devuelve verdadero si el token está en la lista negra
    }
}
