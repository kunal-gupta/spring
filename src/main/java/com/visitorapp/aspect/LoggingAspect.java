package com.visitorapp.aspect;

import com.visitorapp.dto.LoginRequest;
import com.visitorapp.dto.RegisterRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Centralized AOP logging for controller/service layers.
 *
 * Why:
 * - Avoid repetitive entry/exit/latency logging in each class.
 * - Keep cross-cutting concern in one place.
 *
 * Common mistakes:
 * - Logging sensitive fields (password/token) directly.
 * - Logging huge payloads without truncation.
 */
@Aspect
@Component
public class LoggingAspect {

    // Single logger for this aspect class.
    // All AOP-generated messages are emitted from here.
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    // Prevent unbounded log line growth for large request objects.
    private static final int MAX_VALUE_LENGTH = 200;

    // Pointcut for all classes under controller package.
    // Concept: "where" to apply cross-cutting logic.
    @Pointcut("within(com.visitorapp.controller..*)")
    public void controllerPackage() {
    }

    // Pointcut for service layer classes.
    // Typical use: timings, business flow tracing, error monitoring.
    @Pointcut("within(com.visitorapp.service..*)")
    public void servicePackage() {
    }

    /**
     * Around advice wraps method execution:
     * - before -> entry log
     * - after success -> exit + latency log
     * - after failure -> error log + rethrow
     *
     * Common mistake:
     * Swallowing exception in aspect (never do that for business methods).
     * We always rethrow to preserve normal error handling.
     */
    @Around("controllerPackage() || servicePackage()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // ProceedingJoinPoint represents the intercepted method invocation.
        // It gives method metadata + arguments and allows explicit execution via proceed().
        String signature = joinPoint.getSignature().toShortString();
        // Measure total method latency (entry -> exit/error) for observability.
        long start = System.currentTimeMillis();
        // Sanitize every argument to avoid leaking sensitive info in logs.
        String args = Arrays.stream(joinPoint.getArgs())
                .map(this::sanitize)
                .collect(Collectors.joining(", "));

        log.info("Enter {} args=[{}]", signature, args);
        try {
            // proceed() continues the normal method call chain.
            // Without this, the target business method would never execute.
            Object result = joinPoint.proceed();
            long durationMs = System.currentTimeMillis() - start;
            log.info("Exit {} durationMs={}", signature, durationMs);
            return result;
        } catch (Throwable ex) {
            long durationMs = System.currentTimeMillis() - start;
            log.error("Error {} durationMs={} message={}", signature, durationMs, ex.getMessage());
            // Always rethrow so controller/service behavior and error handlers remain correct.
            throw ex;
        }
    }

    /**
     * Additional exception hook for centralized warning logs.
     *
     * Note:
     * This may create an extra line in addition to @Around error log, which is
     * sometimes useful for log searching by exception type.
     */
    @AfterThrowing(pointcut = "controllerPackage() || servicePackage()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        // JoinPoint is read-only context for the already-failed invocation.
        // Unlike ProceedingJoinPoint, it does not support proceed().
        String signature = joinPoint.getSignature().toShortString();
        log.warn("Exception in {} type={} message={}", signature, ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Sanitizes method arguments before logging.
     *
     * Common mistake:
     * Logging DTOs directly may print passwords/tokens/PII.
     */
    private String sanitize(Object arg) {
        if (arg == null) {
            return "null";
        }
        if (arg instanceof LoginRequest request) {
            return "LoginRequest{username='" + trimValue(request.username()) + "', password='***'}";
        }
        if (arg instanceof RegisterRequest request) {
            return "RegisterRequest{username='" + trimValue(request.username()) + "', password='***'}";
        }

        String value = arg.toString();
        return trimValue(value);
    }

    // Trims large text values to keep log files readable and ingestion-friendly.
    private String trimValue(String value) {
        if (value == null) {
            return "null";
        }
        if (value.length() <= MAX_VALUE_LENGTH) {
            return value;
        }
        return value.substring(0, MAX_VALUE_LENGTH) + "...";
    }
}
