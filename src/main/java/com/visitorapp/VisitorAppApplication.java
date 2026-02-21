package com.visitorapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Spring Boot application.
 *
 * Why @SpringBootApplication:
 * 1) @Configuration: this class can define beans.
 * 2) @EnableAutoConfiguration: Spring Boot auto-configures common beans
 *    (DataSource, EntityManager, DispatcherServlet, etc.) based on classpath.
 * 3) @ComponentScan: scans this package and subpackages for @Component types
 *    such as @Controller, @Service, @Repository.
 *
 * Context startup flow:
 * 1) main() calls SpringApplication.run(...)
 * 2) ApplicationContext is created
 * 3) Beans are discovered/instantiated and dependencies are injected
 * 4) Embedded web server starts on configured port (default 8080)
 *
 * Common beginner mistake:
 * Moving this class into a package that is NOT the root package can break
 * component scanning and you may get "No qualifying bean" errors.
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
