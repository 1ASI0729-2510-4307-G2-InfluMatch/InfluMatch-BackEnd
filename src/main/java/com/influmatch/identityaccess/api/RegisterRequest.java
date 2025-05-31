// src/main/java/com/influmatch/identityaccess/api/RegisterRequest.java
package com.influmatch.identityaccess.api;

import jakarta.validation.constraints.*;

/**
 * Registration request DTO with strong validation rules.
 * - Email must be valid format
 * - Password must be at least 8 chars with mix of upper/lower/numbers/special
 * - Role must be either BRAND or INFLUENCER
 */
public record RegisterRequest(
        @NotBlank(message = "email_required")
        @Email(message = "email_invalid", 
              regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        String email,

        @NotBlank(message = "password_required")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "password_weak")
        String password,

        @NotNull(message = "role_required")
        Role role                     // BRAND • INFLUENCER
) {
    public enum Role { BRAND, INFLUENCER }

    /**
     * Validates that the password meets security requirements:
     * - At least 8 characters
     * - Contains at least one digit
     * - Contains at least one lowercase letter
     * - Contains at least one uppercase letter
     * - Contains at least one special character
     * - No whitespace allowed
     */
    public boolean isPasswordValid() {
        return password != null && 
               password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }
}
