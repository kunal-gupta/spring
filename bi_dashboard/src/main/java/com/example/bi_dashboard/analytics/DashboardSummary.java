package com.example.bi_dashboard.analytics;

public record DashboardSummary(
        double revenue,
        double netRevenue,
        long orders,
        long itemsSold,
        double grossProfit,
        double grossMarginPct,
        double refunds,
        double refundRatePct,
        double avgOrderValue
) {
}
