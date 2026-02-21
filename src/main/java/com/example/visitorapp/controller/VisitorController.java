package com.example.visitorapp.controller;

import com.example.visitorapp.dto.VisitLogRequest;
import com.example.visitorapp.dto.VisitResponse;
import com.example.visitorapp.dto.VisitorSummaryResponse;
import com.example.visitorapp.service.VisitorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposes HTTP endpoints for visitor/visit operations.
 */
@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    // Service layer contains business logic.
    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    /**
     * Logs one visit.
     * If person already exists (same phone/email), it adds a new visit to that person.
     */
    @PostMapping("/visits")
    @ResponseStatus(HttpStatus.CREATED)
    public VisitResponse logVisit(@Valid @RequestBody VisitLogRequest request) {
        return visitorService.logVisit(request);
    }

    /**
     * Search visitor by email or phone.
     * Returns profile + last visit + total visits.
     */
    @GetMapping("/search")
    public VisitorSummaryResponse searchVisitor(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {
        return visitorService.findVisitorByPhoneOrEmail(email, phone);
    }

    /**
     * Returns full meeting history for a visitor in descending date order.
     */
    @GetMapping("/{visitorId}/history")
    public List<VisitResponse> getHistory(@PathVariable Long visitorId) {
        return visitorService.getVisitHistory(visitorId);
    }
}
