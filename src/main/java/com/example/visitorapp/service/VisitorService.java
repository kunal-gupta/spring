package com.example.visitorapp.service;

import com.example.visitorapp.dto.VisitLogRequest;
import com.example.visitorapp.dto.VisitResponse;
import com.example.visitorapp.dto.VisitorSummaryResponse;
import com.example.visitorapp.exception.ResourceNotFoundException;
import com.example.visitorapp.model.Visit;
import com.example.visitorapp.model.Visitor;
import com.example.visitorapp.repository.VisitRepository;
import com.example.visitorapp.repository.VisitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer contains business rules.
 * It decides how visitors are matched and how visit history is stored/retrieved.
 */
@Service
public class VisitorService {

    // Repositories are used for database operations.
    private final VisitorRepository visitorRepository;
    private final VisitRepository visitRepository;

    public VisitorService(VisitorRepository visitorRepository, VisitRepository visitRepository) {
        this.visitorRepository = visitorRepository;
        this.visitRepository = visitRepository;
    }

    /**
     * Logs one visit.
     * Transactional ensures visitor + visit save happen together as one unit.
     */
    @Transactional
    public VisitResponse logVisit(VisitLogRequest request) {
        // At least one stable identifier is needed for repeat-visitor detection.
        if (!StringUtils.hasText(request.getEmail()) && !StringUtils.hasText(request.getPhone())) {
            throw new IllegalArgumentException("Either email or phone is required to identify a visitor.");
        }

        // Reuse existing visitor if email/phone already exists; else create new visitor.
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

    /**
     * Resolve visitor identity based on email/phone.
     * If both map to different rows, input is inconsistent and rejected.
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
