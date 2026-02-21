package com.visitorapp.exception;

/**
 * Custom exception used when requested data is not found.
 * GlobalExceptionHandler converts this into HTTP 404.
 *
 * Why custom exception:
 * Makes intent explicit and avoids scattering HTTP concerns in service layer.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
