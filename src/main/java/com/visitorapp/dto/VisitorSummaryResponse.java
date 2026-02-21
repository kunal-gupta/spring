package com.visitorapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response returned by search API.
 * Includes visitor profile + aggregate visit information.
 *
 * API design benefit:
 * Frontend can show summary screen in one call
 * (profile + total visits + last visited).
 */
@Data
public class VisitorSummaryResponse {

    // Visitor master details.
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String designation;
    // Last visit and total number of visits.
    private LocalDateTime lastVisitedAt;
    private long totalVisits;
}
