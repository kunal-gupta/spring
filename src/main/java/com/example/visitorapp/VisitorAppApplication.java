package com.example.visitorapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Spring Boot application.
 * Spring starts from here, creates all beans, and runs the embedded server.
 */
@SpringBootApplication
public class VisitorAppApplication {

    /**
     * Main method called by Java runtime.
     * @param args command line arguments (not used here)
     */
    public static void main(String[] args) {
        SpringApplication.run(VisitorAppApplication.class, args);
    }
}
