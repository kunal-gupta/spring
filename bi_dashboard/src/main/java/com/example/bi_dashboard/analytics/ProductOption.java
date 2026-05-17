package com.example.bi_dashboard.analytics;

/**
 * Represents a single product option available in the dashboard's filtering system.
 *
 * @param productId The unique identifier of the product.
 * @param productName The display name of the product.
 */
public record ProductOption(
        long productId,
        String productName
) {
}
