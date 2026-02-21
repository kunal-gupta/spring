package com.visitorapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response returned after a visit is logged or when history is fetched.
 *
 * Why return DTO instead of entity:
 * You control exactly what client receives and can change internal entity
 * structure later without breaking API contract.
 */
@Data
public class VisitResponse {

    // Visit record ID.
    private Long id;
    // Visitor master ID.
    private Long visitorId;
    // Quick display name for UI convenience.
    private String visitorName;
    // Timestamp of this visit.
    private LocalDateTime visitedAt;
    // Meeting details.
    private String purpose;
    private String notes;
}
