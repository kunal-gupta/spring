package com.example.visitorapp.exception;

/**
 * Custom exception used when requested data is not found.
 * GlobalExceptionHandler converts this into HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
