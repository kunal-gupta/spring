package com.visitorapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Login input payload.
 *
 * Concept:
 * - DTO defines contract of expected JSON body.
 * - Validation annotations give early, readable 400 errors.
 *
 * Common mistakes:
 * - Accepting empty strings and letting service fail later.
 * - Using entity classes as request body instead of dedicated DTO.
 */
public record LoginRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password
) {
}
