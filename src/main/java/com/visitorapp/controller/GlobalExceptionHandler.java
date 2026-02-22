package com.visitorapp.controller;

import com.visitorapp.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Central place to translate Java exceptions into clean HTTP responses.
 * This keeps controller/service code focused on business logic.
 *
 * Why @RestControllerAdvice:
 * Applies to all controllers globally and returns JSON responses.
 * Without this, unhandled exceptions become generic 500 responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Convert "not found" errors into 404 response.
    // Best practice: map domain-specific exception to correct HTTP status code.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(ex.getMessage()));
    }

    // Convert bad input/business validation errors into 400 response.
    // Use IllegalArgumentException carefully; avoid catching Exception broadly.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(errorBody(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody("Invalid username or password"));
    }

    // Convert bean validation errors (@Valid) into field-wise 400 response.
    // Common mistake: forgetting @Valid in controller means this handler never triggers.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    // Helper method for consistent { "error": "..." } responses.
    private Map<String, String> errorBody(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("error", message);
        return body;
    }
}
