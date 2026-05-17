package com.example.bi_dashboard.analytics;

/**
 * Represents a high-level summary of the dashboard's key performance indicators (KPIs).
 * This record encapsulates aggregated business metrics calculated across the entire dataset,
 * providing a snapshot of the business's overall performance.
 *
 * @param revenue The total gross revenue generated from all orders.
 * @param netRevenue The total revenue after deducting refunds.
 * @param orders The total count of orders placed.
 * @param itemsSold The total number of individual items purchased across all orders.
 * @param grossProfit The total revenue minus the cost of goods sold (COGS).
 * @param grossMarginPct The percentage of revenue that is retained as gross profit (Gross Margin = Gross Profit / Revenue).
 * @param refunds The total monetary amount refunded to customers.
 * @param refundRatePct The percentage of the total revenue that was refunded.
 * @param avgOrderValue The average revenue generated per order.
 */
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
