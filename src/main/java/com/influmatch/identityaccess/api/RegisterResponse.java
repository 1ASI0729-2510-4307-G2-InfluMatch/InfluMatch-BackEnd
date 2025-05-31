// src/main/java/com/influmatch/identityaccess/api/RegisterResponse.java
package com.influmatch.identityaccess.api;

/**
 * Response object for successful registration.
 * Contains user details and authentication token.
 */
public record RegisterResponse(
    Long userId,
    String email,
    String role,
    String token,
    String message
) {}
