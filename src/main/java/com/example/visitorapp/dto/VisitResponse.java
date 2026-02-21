package com.example.visitorapp.dto;

import java.time.LocalDateTime;

/**
 * Response returned after a visit is logged or when history is fetched.
 */
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

    // Standard getters/setters for JSON serialization.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public LocalDateTime getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(LocalDateTime visitedAt) {
        this.visitedAt = visitedAt;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
