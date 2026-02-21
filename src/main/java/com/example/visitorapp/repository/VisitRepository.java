package com.example.visitorapp.repository;

import com.example.visitorapp.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Visit table.
 * Contains helper queries for history and "last visit" features.
 */
public interface VisitRepository extends JpaRepository<Visit, Long> {

    // Get full history of a visitor, newest first.
    List<Visit> findByVisitorIdOrderByVisitedAtDesc(Long visitorId);

    // Get the latest visit of a visitor.
    Optional<Visit> findTopByVisitorIdOrderByVisitedAtDesc(Long visitorId);

    // Count how many times the visitor has come.
    long countByVisitorId(Long visitorId);
}
