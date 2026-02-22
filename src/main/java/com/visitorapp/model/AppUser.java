package com.visitorapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Application user used for authentication.
 *
 * Concept:
 * - Separate auth user model from business models (Visitor/Visit).
 * - Role is stored as authority string (example: ROLE_USER).
 *
 * Common mistakes:
 * - Saving plain-text passwords (always store encoded hash).
 * - Reusing domain entities (like Visitor) as login users.
 */
@Entity
@Table(name = "app_users")
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // Store BCrypt hash, never the raw password.
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
}
