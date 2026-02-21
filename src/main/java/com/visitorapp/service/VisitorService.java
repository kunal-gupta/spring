package com.visitorapp.service;

import com.visitorapp.dto.VisitLogRequest;
import com.visitorapp.dto.VisitResponse;
import com.visitorapp.dto.VisitorSummaryResponse;
import com.visitorapp.exception.ResourceNotFoundException;
import com.visitorapp.model.Visit;
import com.visitorapp.model.Visitor;
import com.visitorapp.repository.VisitRepository;
import com.visitorapp.repository.VisitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer contains business rules.
 * It decides how visitors are matched and how visit history is stored/retrieved.
 *
 * Why @Service:
 * Marks this class as a Spring-managed business bean.
 * It becomes part of ApplicationContext through component scanning.
 *
 * Why this layer exists:
 * Keep controller focused on HTTP details and move business decisions here.
 */
@Service
public class VisitorService {

    // Repositories are used for database operations.
    private final VisitorRepository visitorRepository;
    private final VisitRepository visitRepository;

    /**
     * Constructor injection for repositories.
     * Spring creates repository beans automatically for interfaces extending
     * JpaRepository and injects them here.
     *
     * Best practice:
     * Prefer constructor injection over field injection for testability and
     * immutability (final fields).
     */
    public VisitorService(VisitorRepository visitorRepository, VisitRepository visitRepository) {
        this.visitorRepository = visitorRepository;
        this.visitRepository = visitRepository;
    }

    /**
     * Logs one visit.
     * Transactional ensures visitor + visit save happen together as one unit.
     *
     * Why @Transactional:
     * If any error happens after saving visitor but before saving visit, whole
     * transaction rolls back, preventing partial/inconsistent data.
     *
     * Common mistake:
     * Doing multiple DB writes without a transaction can leave half-saved data.
     */
    @Transactional
    public VisitResponse logVisit(VisitLogRequest request) {
        // At least one stable identifier is needed for repeat-visitor detection.
        if (!StringUtils.hasText(request.getEmail()) && !StringUtils.hasText(request.getPhone())) {
            throw new IllegalArgumentException("Either email or phone is required to identify a visitor.");
        }

        // Reuse existing visitor if email/phone already exists; else create new visitor.
        // This avoids duplicate master records for same person.
        Visitor visitor = resolveVisitor(request);
        // Update latest profile details every time a person visits.
        updateVisitorDetails(visitor, request);
        Visitor savedVisitor = visitorRepository.save(visitor);

        // Create a separate visit record so full history is preserved.
        Visit visit = new Visit();
        visit.setVisitor(savedVisitor);
        // If client does not send visit timestamp, use current server time.
        visit.setVisitedAt(request.getVisitedAt() != null ? request.getVisitedAt() : LocalDateTime.now());
        visit.setPurpose(request.getPurpose());
        visit.setNotes(request.getNotes());

        Visit savedVisit = visitRepository.save(visit);
        return toVisitResponse(savedVisit);
    }

    /**
     * Search visitor by email or phone and return summary data.
     *
     * Why readOnly = true:
     * Hints Spring/Hibernate that this method does not modify state.
     * Can improve performance and prevents accidental write intent.
     */
    @Transactional(readOnly = true)
    public VisitorSummaryResponse findVisitorByPhoneOrEmail(String email, String phone) {
        // Reject invalid search where neither identifier is provided.
        if (!StringUtils.hasText(email) && !StringUtils.hasText(phone)) {
            throw new IllegalArgumentException("Provide either email or phone to search.");
        }

        Visitor visitor = findVisitor(email, phone)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor not found."));

        return toVisitorSummary(visitor);
    }

    /**
     * Returns all past visits of one visitor in reverse chronological order.
     *
     * Failure case:
     * Returning empty list for unknown visitor can hide client bugs.
     * We explicitly throw 404 when visitor id is invalid.
     */
    @Transactional(readOnly = true)
    public List<VisitResponse> getVisitHistory(Long visitorId) {
        // Fail fast with clear message if visitor ID does not exist.
        if (!visitorRepository.existsById(visitorId)) {
            throw new ResourceNotFoundException("Visitor not found with id: " + visitorId);
        }

        return visitRepository.findByVisitorIdOrderByVisitedAtDesc(visitorId)
                .stream()
                .map(this::toVisitResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Visitor findByPhone(String phone) {
        return visitorRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor not found with phone: " + phone));
    }

    @Transactional(readOnly = true)
    public Visitor findByEmailAndPhone(String email, String phone) {
        return visitorRepository.findByEmailIgnoreCaseAndPhone(email, phone)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor not found for provided email and phone."));
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return visitorRepository.existsByEmailIgnoreCase(email);
    }

    @Transactional
    public long deleteByPhone(String phone) {
        Optional<Visitor> existing = visitorRepository.findByPhone(phone);
        if (existing.isEmpty()) {
            return 0;
        }
        long totalVisits = visitRepository.countByVisitorId(existing.get().getId());
        if (totalVisits > 0) {
            throw new IllegalArgumentException("Cannot delete visitor with existing visit history.");
        }
        return visitorRepository.deleteByPhone(phone);
    }

    @Transactional(readOnly = true)
    public List<Visitor> findByIds(List<Long> ids) {
        return visitorRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<Visitor> searchByName(String text) {
        return visitorRepository.findByNameContainingIgnoreCase(text);
    }

    @Transactional(readOnly = true)
    public List<Visitor> findWithoutEmail() {
        return visitorRepository.findByEmailIsNull();
    }

    @Transactional(readOnly = true)
    public List<Visitor> top5ByName() {
        return visitorRepository.findTop5ByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Visitor> top10Latest() {
        return visitorRepository.findTop10ByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public VisitResponse firstVisit(Long visitorId) {
        Visit visit = visitRepository.findFirstByVisitorIdOrderByVisitedAtAsc(visitorId)
                .orElseThrow(() -> new ResourceNotFoundException("No visit history found for visitor id: " + visitorId));
        return toVisitResponse(visit);
    }

    @Transactional(readOnly = true)
    public List<VisitResponse> visitsBetween(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' must be before or equal to 'to'.");
        }
        return visitRepository.findByVisitedAtBetween(from, to)
                .stream()
                .map(this::toVisitResponse)
                .toList();
    }

    /**
     * Resolve visitor identity based on email/phone.
     * If both map to different rows, input is inconsistent and rejected.
     *
     * Real-world edge case handled:
     * User provides email of person A and phone of person B accidentally.
     * We fail fast with 400 instead of guessing and corrupting data.
     */
    private Visitor resolveVisitor(VisitLogRequest request) {
        Optional<Visitor> existingByEmail = StringUtils.hasText(request.getEmail())
                ? visitorRepository.findByEmailIgnoreCase(request.getEmail().trim())
                : Optional.empty();
        Optional<Visitor> existingByPhone = StringUtils.hasText(request.getPhone())
                ? visitorRepository.findByPhone(request.getPhone().trim())
                : Optional.empty();

        if (existingByEmail.isPresent() && existingByPhone.isPresent()
                && !existingByEmail.get().getId().equals(existingByPhone.get().getId())) {
            throw new IllegalArgumentException("Email and phone belong to different visitors.");
        }

        return existingByEmail.or(() -> existingByPhone).orElseGet(Visitor::new);
    }

    /**
     * Helper used by search API: try email first, then phone.
     *
     * Best approach:
     * Apply trim() before lookup to avoid mismatch due to leading/trailing spaces.
     */
    private Optional<Visitor> findVisitor(String email, String phone) {
        Optional<Visitor> byEmail = StringUtils.hasText(email)
                ? visitorRepository.findByEmailIgnoreCase(email.trim())
                : Optional.empty();
        if (byEmail.isPresent()) {
            return byEmail;
        }
        return StringUtils.hasText(phone)
                ? visitorRepository.findByPhone(phone.trim())
                : Optional.empty();
    }

    /**
     * Copy request profile fields into Visitor entity.
     *
     * Common mistake:
     * Forgetting to normalize input (trim/blank checks) causes duplicate records
     * like "alice@example.com" vs " alice@example.com ".
     */
    private void updateVisitorDetails(Visitor visitor, VisitLogRequest request) {
        visitor.setName(request.getName().trim());
        visitor.setAddress(request.getAddress());
        visitor.setDesignation(request.getDesignation());
        visitor.setEmail(StringUtils.hasText(request.getEmail()) ? request.getEmail().trim() : null);
        visitor.setPhone(StringUtils.hasText(request.getPhone()) ? request.getPhone().trim() : null);
    }

    /**
     * Maps Visit entity to API response DTO.
     *
     * Why map to DTO:
     * Avoid exposing JPA entities directly over API and keep response stable.
     */
    private VisitResponse toVisitResponse(Visit visit) {
        VisitResponse response = new VisitResponse();
        response.setId(visit.getId());
        response.setVisitorId(visit.getVisitor().getId());
        response.setVisitorName(visit.getVisitor().getName());
        response.setVisitedAt(visit.getVisitedAt());
        response.setPurpose(visit.getPurpose());
        response.setNotes(visit.getNotes());
        return response;
    }

    /**
     * Builds summary with aggregate values (last visit and count).
     *
     * Note:
     * This method performs aggregate queries from VisitRepository so the UI can
     * render summary quickly without multiple API calls.
     */
    private VisitorSummaryResponse toVisitorSummary(Visitor visitor) {
        VisitorSummaryResponse response = new VisitorSummaryResponse();
        response.setId(visitor.getId());
        response.setName(visitor.getName());
        response.setAddress(visitor.getAddress());
        response.setEmail(visitor.getEmail());
        response.setPhone(visitor.getPhone());
        response.setDesignation(visitor.getDesignation());
        response.setTotalVisits(visitRepository.countByVisitorId(visitor.getId()));
        response.setLastVisitedAt(visitRepository.findTopByVisitorIdOrderByVisitedAtDesc(visitor.getId())
                .map(Visit::getVisitedAt)
                .orElse(null));
        return response;
    }
}
