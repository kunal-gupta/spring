package com.example.visitorapp.dto;

import java.time.LocalDateTime;

/**
 * Response returned by search API.
 * Includes visitor profile + aggregate visit information.
 */
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

    // Standard getters/setters for JSON serialization.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getLastVisitedAt() {
        return lastVisitedAt;
    }

    public void setLastVisitedAt(LocalDateTime lastVisitedAt) {
        this.lastVisitedAt = lastVisitedAt;
    }

    public long getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(long totalVisits) {
        this.totalVisits = totalVisits;
    }
}
