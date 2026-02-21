package com.visitorapp.controller;

import com.visitorapp.dto.VisitLogRequest;
import com.visitorapp.dto.VisitResponse;
import com.visitorapp.dto.VisitorSummaryResponse;
import com.visitorapp.model.Visitor;
import com.visitorapp.service.VisitorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST controller exposes HTTP endpoints for visitor/visit operations.
 *
 * Why @RestController:
 * Combines @Controller + @ResponseBody, so returned Java objects are
 * automatically converted to JSON by Jackson.
 *
 * Why @RequestMapping("/api/visitors"):
 * Defines a common URL prefix for all methods in this controller.
 *
 * Bean creation:
 * This class is a Spring bean because @RestController is a stereotype
 * annotation detected by component scanning.
 */
@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    // Service layer contains business logic.
    private final VisitorService visitorService;

    /**
     * Constructor injection (recommended).
     * Spring injects VisitorService automatically because there is exactly one
     * constructor; explicit @Autowired is optional in this case.
     *
     * Common mistake:
     * Creating service with "new VisitorService(...)" manually bypasses Spring
     * and breaks dependency injection/proxy features.
     */
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    /**
     * Logs one visit.
     * If person already exists (same phone/email), it adds a new visit to that person.
     *
     * Why @PostMapping:
     * POST is used when creating new resources (visit entries).
     *
     * Why @Valid + @RequestBody:
     * - @RequestBody maps incoming JSON to VisitLogRequest.
     * - @Valid triggers bean validation annotations in DTO.
     *
     * Failure cases:
     * - Invalid JSON format -> 400 Bad Request
     * - Validation fail (@NotBlank/@Email) -> handled by GlobalExceptionHandler
     */
    @PostMapping("/visits")
    @ResponseStatus(HttpStatus.CREATED)
    public VisitResponse logVisit(@Valid @RequestBody VisitLogRequest request) {
        return visitorService.logVisit(request);
    }

    /**
     * Search visitor by email or phone.
     * Returns profile + last visit + total visits.
     *
     * Why @RequestParam(required = false):
     * Allows optional query parameters. Service enforces at least one
     * identifier, so controller stays thin.
     *
     * Example:
     * /api/visitors/search?email=a@b.com
     * /api/visitors/search?phone=9999999999
     */
    @GetMapping("/search")
    public VisitorSummaryResponse searchVisitor(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {
        return visitorService.findVisitorByPhoneOrEmail(email, phone);
    }

    /**
     * Returns full meeting history for a visitor in descending date order.
     *
     * Why @PathVariable:
     * Binds {visitorId} from URL to method argument.
     *
     * Failure case:
     * If visitor does not exist, service throws ResourceNotFoundException -> 404.
     */
    @GetMapping("/{visitorId}/history")
    public List<VisitResponse> getHistory(@PathVariable Long visitorId) {
        return visitorService.getVisitHistory(visitorId);
    }

    // Reference endpoints for derived-query method examples.

    @GetMapping("/reference/by-phone")
    public Visitor findByPhone(@RequestParam String phone) {
        return visitorService.findByPhone(phone);
    }

    @GetMapping("/reference/by-email-and-phone")
    public Visitor findByEmailAndPhone(@RequestParam String email, @RequestParam String phone) {
        return visitorService.findByEmailAndPhone(email, phone);
    }

    @GetMapping("/reference/exists-by-email")
    public Map<String, Boolean> existsByEmail(@RequestParam String email) {
        return Map.of("exists", visitorService.existsByEmail(email));
    }

    @DeleteMapping("/reference/by-phone")
    public Map<String, Long> deleteByPhone(@RequestParam String phone) {
        return Map.of("deletedCount", visitorService.deleteByPhone(phone));
    }

    @GetMapping("/reference/by-ids")
    public List<Visitor> findByIds(@RequestParam List<Long> ids) {
        return visitorService.findByIds(ids);
    }

    @GetMapping("/reference/search-by-name")
    public List<Visitor> searchByName(@RequestParam String text) {
        return visitorService.searchByName(text);
    }

    @GetMapping("/reference/without-email")
    public List<Visitor> findWithoutEmail() {
        return visitorService.findWithoutEmail();
    }

    @GetMapping("/reference/top5-by-name")
    public List<Visitor> top5ByName() {
        return visitorService.top5ByName();
    }

    @GetMapping("/reference/top10-latest")
    public List<Visitor> top10Latest() {
        return visitorService.top10Latest();
    }

    @GetMapping("/reference/{visitorId}/first-visit")
    public VisitResponse firstVisit(@PathVariable Long visitorId) {
        return visitorService.firstVisit(visitorId);
    }

    @GetMapping("/reference/visits-between")
    public List<VisitResponse> visitsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return visitorService.visitsBetween(from, to);
    }
}
