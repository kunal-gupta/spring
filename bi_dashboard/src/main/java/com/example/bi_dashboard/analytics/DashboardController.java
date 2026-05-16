package com.example.bi_dashboard.analytics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final AnalyticsService analyticsService;

    public DashboardController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public DashboardSummary summary() throws SQLException {
        return analyticsService.getSummary();
    }

    @GetMapping("/sales-trend")
    public List<SalesTrendPoint> salesTrend() throws SQLException {
        return analyticsService.getSalesTrend();
    }

    @GetMapping("/metadata")
    public DashboardMetadata metadata() throws SQLException {
        return analyticsService.getMetadata();
    }
}
