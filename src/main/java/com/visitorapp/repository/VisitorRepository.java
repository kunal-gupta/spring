package com.visitorapp.repository;

import com.visitorapp.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Visitor table.
 * Spring Data JPA auto-implements these methods based on method names.
 *
 * Why no implementation class:
 * Spring generates proxy implementations at runtime and registers them as beans.
 *
 * Autowiring path:
 * VisitorRepository bean -> injected into VisitorService constructor.
 */
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    // Find a visitor by email (case-insensitive).
    Optional<Visitor> findByEmailIgnoreCase(String email);

    // Find a visitor by phone.
    Optional<Visitor> findByPhone(String phone);

    // Find a visitor by both email and phone.
    Optional<Visitor> findByEmailIgnoreCaseAndPhone(String email, String phone);

    // Check if a visitor exists for the given email.
    boolean existsByEmailIgnoreCase(String email);

    // Delete visitor by phone and return affected row count.
    long deleteByPhone(String phone);

    // Fetch visitors whose ids are in provided list.
    List<Visitor> findByIdIn(List<Long> ids);

    // Name contains text (case-insensitive).
    List<Visitor> findByNameContainingIgnoreCase(String text);

    // Find visitors where email is null.
    List<Visitor> findByEmailIsNull();

    // Top N examples using ordering.
    List<Visitor> findTop5ByOrderByNameAsc();

    List<Visitor> findTop10ByOrderByIdDesc();
}
