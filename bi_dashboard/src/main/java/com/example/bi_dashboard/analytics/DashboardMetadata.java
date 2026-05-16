package com.example.bi_dashboard.analytics;

import java.util.List;

public record DashboardMetadata(
        String minDate,
        String maxDate,
        List<ProductOption> products
) {
}
