package com.visitorapp.repository;

import com.visitorapp.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Visit table.
 * Contains helper queries for history and "last visit" features.
 *
 * Method naming convention:
 * Spring parses method names (findTopBy..., countBy...) and creates SQL/JPA
 * queries automatically.
 *
 * Common mistake:
 * Typo in property name inside method (e.g., findByVisitrId...) fails at startup.
 */
public interface VisitRepository extends JpaRepository<Visit, Long> {

    // Get full history of a visitor, newest first.
    List<Visit> findByVisitorIdOrderByVisitedAtDesc(Long visitorId);

    // Get the latest visit of a visitor.
    Optional<Visit> findTopByVisitorIdOrderByVisitedAtDesc(Long visitorId);

    // Count how many times the visitor has come.
    long countByVisitorId(Long visitorId);

    // Get first (oldest) visit of a visitor.
    Optional<Visit> findFirstByVisitorIdOrderByVisitedAtAsc(Long visitorId);

    // Get visits in a time range.
    List<Visit> findByVisitedAtBetween(LocalDateTime from, LocalDateTime to);
}
