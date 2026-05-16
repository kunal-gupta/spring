package com.example.bi_dashboard.analytics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsServiceTest {
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() throws Exception {
        analyticsService = new AnalyticsService();
        analyticsService.initialize();
    }

    @Test
    void computesDatasetSummary() throws Exception {
        DashboardSummary summary = analyticsService.getSummary();

        assertThat(summary.orders()).isEqualTo(32313);
        assertThat(summary.revenue()).isGreaterThan(0);
        assertThat(summary.netRevenue()).isLessThan(summary.revenue());
        assertThat(summary.grossMarginPct()).isGreaterThan(0);
    }

    @Test
    void returnsChronologicalTrendAndMetadata() throws Exception {
        assertThat(analyticsService.getSalesTrend()).isNotEmpty();
        assertThat(analyticsService.getSalesTrend().get(0).month()).isEqualTo("2012-03");
        assertThat(analyticsService.getMetadata().products()).hasSize(4);
        assertThat(analyticsService.getMetadata().minDate()).isEqualTo("2012-03-19");
    }
}
