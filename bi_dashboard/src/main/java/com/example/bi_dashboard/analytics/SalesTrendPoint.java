package com.example.bi_dashboard.analytics;

/**
 * Represents aggregated business performance data for a specific time period (typically a month).
 * This record is used to plot trends on the dashboard's charts over time.
 *
 * @param month The month the data applies to (formatted as YYYY-MM).
 * @param revenue The total gross revenue generated during the month.
 * @param netRevenue The total net revenue (gross revenue minus refunds) for the month.
 * @param orders The total count of orders placed during the month.
 * @param avgOrderValue The average order value during the month.
 */
public record SalesTrendPoint(
        String month,
        double revenue,
        double netRevenue,
        long orders,
        double avgOrderValue
) {
}
