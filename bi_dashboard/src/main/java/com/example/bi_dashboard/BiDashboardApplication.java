package com.example.bi_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the BI Dashboard Spring Boot backend application.
 * This class uses the {@link SpringBootApplication} annotation to enable Spring Boot's
 * auto-configuration, component scanning, and application properties support.
 * It serves the REST API consumed by the frontend.
 */
@SpringBootApplication
public class BiDashboardApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        System.out.println("bi_dashboard started");
        SpringApplication.run(BiDashboardApplication.class, args);
    }
}
