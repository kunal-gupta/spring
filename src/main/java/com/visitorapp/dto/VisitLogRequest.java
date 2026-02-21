package com.visitorapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Request body used when logging a new visit.
 * This combines visitor profile data + visit instance data.
 *
 * DTO purpose:
 * Keep API contract separate from entity classes.
 * This avoids exposing internal DB model directly to clients.
 */
@Data
public class VisitLogRequest {

    // Mandatory name for display and identification.
    // @NotBlank rejects null, empty, and whitespace-only values.
    @NotBlank
    private String name;

    // Optional profile fields.
    private String address;

    private String phone;

    // If provided, must be a valid email format.
    // Note: @Email allows null by default; combine with @NotBlank if required.
    @Email
    private String email;

    private String designation;

    // Optional visit-specific fields.
    private String purpose;

    private String notes;

    // Optional explicit visit date-time (uses now() if not provided).
    // Common mistake: sending wrong JSON datetime format causes parse error (400).
    private LocalDateTime visitedAt;
}
