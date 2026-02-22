package com.visitorapp.repository;

import com.visitorapp.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for auth users.
 *
 * Concept:
 * - Provides user lookup needed by Spring Security authentication flow.
 *
 * Common mistakes:
 * - Using case-sensitive username lookup unintentionally.
 * - Doing manual SQL in service when derived query method is enough.
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    // Login lookup by username.
    Optional<AppUser> findByUsernameIgnoreCase(String username);

    // Fast existence check used in registration.
    boolean existsByUsernameIgnoreCase(String username);
}
