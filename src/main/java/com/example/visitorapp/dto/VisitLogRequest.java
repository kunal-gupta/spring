package com.example.visitorapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Request body used when logging a new visit.
 * This combines visitor profile data + visit instance data.
 */
public class VisitLogRequest {

    // Mandatory name for display and identification.
    @NotBlank
    private String name;

    // Optional profile fields.
    private String address;

    private String phone;

    // If provided, must be a valid email format.
    @Email
    private String email;

    private String designation;

    // Optional visit-specific fields.
    private String purpose;

    private String notes;

    // Optional explicit visit date-time (uses now() if not provided).
    private LocalDateTime visitedAt;

    // Standard getters/setters for JSON binding.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public LocalDateTime getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(LocalDateTime visitedAt) {
        this.visitedAt = visitedAt;
    }
}
