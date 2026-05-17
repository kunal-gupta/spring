package com.example.bi_dashboard.analytics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

/**
 * REST Controller responsible for exposing dashboard analytics data to the frontend client.
 * It defines the API endpoints under the "/api/dashboard" route.
 * Spring's {@link RestController} annotation ensures that the responses are automatically
 * serialized into JSON format.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final AnalyticsService analyticsService;

    /**
     * Constructs the DashboardController with the required service dependency.
     * Spring automatically injects the {@link AnalyticsService} instance.
     *
     * @param analyticsService The service that handles the underlying data querying and business logic.
     */
    public DashboardController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * Endpoint to retrieve high-level key performance indicators (KPIs).
     *
     * @return A {@link DashboardSummary} object containing the aggregated metrics.
     * @throws SQLException If an error occurs while querying the database.
     */
    @GetMapping("/summary")
    public DashboardSummary summary() throws SQLException {
        return analyticsService.getSummary();
    }

    /**
     * Endpoint to retrieve sales performance trends over time.
     *
     * @return A list of {@link SalesTrendPoint} objects, each representing metrics for a specific month.
     * @throws SQLException If an error occurs while querying the database.
     */
    @GetMapping("/sales-trend")
    public List<SalesTrendPoint> salesTrend() throws SQLException {
        return analyticsService.getSalesTrend();
    }

    /**
     * Endpoint to retrieve dashboard metadata, such as date ranges and product lists.
     * This is typically used by the frontend to populate filters and date pickers.
     *
     * @return A {@link DashboardMetadata} object containing the available context.
     * @throws SQLException If an error occurs while querying the database.
     */
    @GetMapping("/metadata")
    public DashboardMetadata metadata() throws SQLException {
        return analyticsService.getMetadata();
    }
}
