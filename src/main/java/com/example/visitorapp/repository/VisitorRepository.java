package com.example.visitorapp.repository;

import com.example.visitorapp.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for Visitor table.
 * Spring Data JPA auto-implements these methods based on method names.
 */
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    // Find a visitor by email (case-insensitive).
    Optional<Visitor> findByEmailIgnoreCase(String email);

    // Find a visitor by phone.
    Optional<Visitor> findByPhone(String phone);
}
