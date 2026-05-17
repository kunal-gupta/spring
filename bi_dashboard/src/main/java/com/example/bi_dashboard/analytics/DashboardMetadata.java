package com.example.bi_dashboard.analytics;

import java.util.List;

/**
 * Encapsulates the metadata required to initialize the dashboard's filters and general context.
 * This includes the date range of the available data and the list of available products.
 *
 * @param minDate The earliest date for which order data is available (formatted as YYYY-MM-DD).
 * @param maxDate The most recent date for which order data is available (formatted as YYYY-MM-DD).
 * @param products A list of {@link ProductOption} objects representing all products available for filtering.
 */
public record DashboardMetadata(
        String minDate,
        String maxDate,
        List<ProductOption> products
) {
}
