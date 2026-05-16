package com.example.bi_dashboard.analytics;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    private static final String DATASET_ROOT = "dataset/Maven+Fuzzy+Factory/";
    private Connection connection;

    @PostConstruct
    void initialize() throws SQLException, IOException {
        connection = DriverManager.getConnection("jdbc:duckdb:");
        Path datasetDirectory = copyDatasetToTempDirectory();

        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE orders AS
                    SELECT * FROM read_csv_auto('%s/orders.csv', header = true);
                    """.formatted(normalize(datasetDirectory)));
            statement.execute("""
                    CREATE TABLE order_items AS
                    SELECT * FROM read_csv_auto('%s/order_items.csv', header = true);
                    """.formatted(normalize(datasetDirectory)));
            statement.execute("""
                    CREATE TABLE order_item_refunds AS
                    SELECT * FROM read_csv_auto('%s/order_item_refunds.csv', header = true);
                    """.formatted(normalize(datasetDirectory)));
            statement.execute("""
                    CREATE TABLE products AS
                    SELECT * FROM read_csv_auto('%s/products.csv', header = true);
                    """.formatted(normalize(datasetDirectory)));
            statement.execute("""
                    CREATE VIEW vw_order_facts AS
                    SELECT
                        o.order_id,
                        CAST(o.created_at AS TIMESTAMP) AS created_at,
                        DATE_TRUNC('month', CAST(o.created_at AS TIMESTAMP)) AS order_month,
                        o.items_purchased,
                        o.price_usd AS revenue,
                        o.cogs_usd AS cogs,
                        o.price_usd - o.cogs_usd AS gross_profit,
                        COALESCE(r.refund_amount, 0) AS refunds,
                        o.price_usd - COALESCE(r.refund_amount, 0) AS net_revenue
                    FROM orders o
                    LEFT JOIN (
                        SELECT order_id, SUM(refund_amount_usd) AS refund_amount
                        FROM order_item_refunds
                        GROUP BY order_id
                    ) r ON o.order_id = r.order_id;
                    """);
            statement.execute("""
                    CREATE VIEW vw_monthly_business_performance AS
                    SELECT
                        order_month,
                        COUNT(*) AS orders,
                        SUM(items_purchased) AS items_sold,
                        SUM(revenue) AS revenue,
                        SUM(cogs) AS cogs,
                        SUM(gross_profit) AS gross_profit,
                        SUM(refunds) AS refunds,
                        SUM(net_revenue) AS net_revenue,
                        AVG(revenue) AS avg_order_value
                    FROM vw_order_facts
                    GROUP BY order_month
                    ORDER BY order_month;
                    """);
        }
    }

    public DashboardSummary getSummary() throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("""
                     SELECT
                         SUM(revenue) AS revenue,
                         SUM(net_revenue) AS net_revenue,
                         COUNT(*) AS orders,
                         SUM(items_purchased) AS items_sold,
                         SUM(gross_profit) AS gross_profit,
                         CASE WHEN SUM(revenue) = 0 THEN 0 ELSE SUM(gross_profit) / SUM(revenue) * 100 END AS gross_margin_pct,
                         SUM(refunds) AS refunds,
                         CASE WHEN SUM(revenue) = 0 THEN 0 ELSE SUM(refunds) / SUM(revenue) * 100 END AS refund_rate_pct,
                         AVG(revenue) AS avg_order_value
                     FROM vw_order_facts;
                     """)) {
            rs.next();
            return new DashboardSummary(
                    rs.getDouble("revenue"),
                    rs.getDouble("net_revenue"),
                    rs.getLong("orders"),
                    rs.getLong("items_sold"),
                    rs.getDouble("gross_profit"),
                    round1(rs.getDouble("gross_margin_pct")),
                    rs.getDouble("refunds"),
                    round1(rs.getDouble("refund_rate_pct")),
                    rs.getDouble("avg_order_value")
            );
        }
    }

    public List<SalesTrendPoint> getSalesTrend() throws SQLException {
        List<SalesTrendPoint> trend = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("""
                     SELECT
                         STRFTIME(order_month, '%Y-%m') AS month,
                         revenue,
                         net_revenue,
                         orders,
                         avg_order_value
                     FROM vw_monthly_business_performance;
                     """)) {
            while (rs.next()) {
                trend.add(new SalesTrendPoint(
                        rs.getString("month"),
                        rs.getDouble("revenue"),
                        rs.getDouble("net_revenue"),
                        rs.getLong("orders"),
                        rs.getDouble("avg_order_value")
                ));
            }
        }
        return trend;
    }

    public DashboardMetadata getMetadata() throws SQLException {
        String minDate;
        String maxDate;
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("""
                     SELECT
                         STRFTIME(MIN(CAST(created_at AS TIMESTAMP)), '%Y-%m-%d') AS min_date,
                         STRFTIME(MAX(CAST(created_at AS TIMESTAMP)), '%Y-%m-%d') AS max_date
                     FROM orders;
                     """)) {
            rs.next();
            minDate = rs.getString("min_date");
            maxDate = rs.getString("max_date");
        }

        List<ProductOption> products = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("""
                     SELECT product_id, product_name
                     FROM products
                     ORDER BY product_id;
                     """)) {
            while (rs.next()) {
                products.add(new ProductOption(rs.getLong("product_id"), rs.getString("product_name")));
            }
        }
        return new DashboardMetadata(minDate, maxDate, products);
    }

    private Path copyDatasetToTempDirectory() throws IOException {
        Path tempDirectory = Files.createTempDirectory("bi-dashboard-dataset");
        for (String fileName : List.of("orders.csv", "order_items.csv", "order_item_refunds.csv", "products.csv")) {
            ClassPathResource resource = new ClassPathResource(DATASET_ROOT + fileName);
            Files.copy(resource.getInputStream(), tempDirectory.resolve(fileName));
        }
        return tempDirectory;
    }

    private String normalize(Path path) {
        return path.toAbsolutePath().toString().replace("\\", "/");
    }

    private double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
