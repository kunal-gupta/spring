package com.example.bi_dashboard.analytics;

public record SalesTrendPoint(
        String month,
        double revenue,
        double netRevenue,
        long orders,
        double avgOrderValue
) {
}
