package com.visitorapp.service;

import com.visitorapp.dto.AuthResponse;
import com.visitorapp.dto.LoginRequest;
import com.visitorapp.dto.RegisterRequest;
import com.visitorapp.model.AppUser;
import com.visitorapp.repository.AppUserRepository;
import com.visitorapp.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication business layer.
 *
 * Concept:
 * - register(): validate uniqueness, hash password, save user, issue JWT.
 * - login(): delegate credential validation to AuthenticationManager, then issue JWT.
 *
 * Common mistakes:
 * - Manually comparing raw password with hash (use AuthenticationManager/PasswordEncoder).
 * - Duplicating username checks in controller and service inconsistently.
 */
@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // Creates a user with ROLE_USER and returns token for immediate API usage.
    public AuthResponse register(RegisterRequest request) {
        if (appUserRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(request.username());
        appUser.setPassword(passwordEncoder.encode(request.password()));
        appUser.setRole("ROLE_USER");

        AppUser savedUser = appUserRepository.save(appUser);
        String token = jwtService.generateToken(savedUser.getUsername(), savedUser.getRole());

        return new AuthResponse(token, "Bearer", savedUser.getUsername(), savedUser.getRole());
    }

    // Authenticates credentials using Spring Security and issues token.
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("ROLE_USER");

        String token = jwtService.generateToken(authentication.getName(), role);
        return new AuthResponse(token, "Bearer", authentication.getName(), role);
    }
}
