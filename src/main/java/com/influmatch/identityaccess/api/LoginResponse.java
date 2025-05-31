package com.influmatch.identityaccess.api;

/**
 * Response DTO for successful login
 */
public record LoginResponse(
    Long userId,
    String email,
    String role,
    String token,
    String message
) {} 