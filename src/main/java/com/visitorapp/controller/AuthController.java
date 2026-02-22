package com.visitorapp.controller;

import com.visitorapp.dto.AuthResponse;
import com.visitorapp.dto.LoginRequest;
import com.visitorapp.dto.RegisterRequest;
import com.visitorapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoints.
 *
 * Concept:
 * - Keep controller thin: only HTTP mapping + validation.
 * - Business/security decisions stay in AuthService.
 *
 * Common mistakes:
 * - Putting token creation logic directly in controller.
 * - Forgetting @Valid so bean-validation on request DTOs never runs.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    // Service contains register/login logic and JWT creation.
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register a new user and return JWT immediately.
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // Authenticate existing user and issue JWT.
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
