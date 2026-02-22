package com.visitorapp.dto;

/**
 * Response returned after successful register/login.
 *
 * Concept:
 * - API returns a token payload that client can store and send in Authorization header.
 *
 * Common mistakes:
 * - Returning internal entity objects directly in auth responses.
 * - Returning raw password hash by mistake (never include password fields in response DTOs).
 */
public record AuthResponse(
        String token,
        String tokenType,
        String username,
        String role
) {
}
