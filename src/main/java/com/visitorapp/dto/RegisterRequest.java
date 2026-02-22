package com.visitorapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Registration input payload.
 *
 * Concept:
 * - Keep minimum password policy close to API boundary with validation.
 *
 * Common mistakes:
 * - No min-length check, allowing weak passwords in development that leak to production.
 * - Trying to hash password in controller/DTO layer.
 */
public record RegisterRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password
) {
}
