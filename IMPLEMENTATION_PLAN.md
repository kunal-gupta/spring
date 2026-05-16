# BI Dashboard Implementation Plan

## 1. Product Goal

Build a rich, story-driven BI application over the Maven Fuzzy Factory commerce dataset. The first release should answer the executive questions this dataset can honestly support:

- How much revenue did the business generate?
- Is the business growing over time?
- Which products drive revenue and profit?
- How healthy are margins?
- How much value is lost to refunds?
- Which raw records explain the summary numbers?

The current dataset supports commerce, product, profitability, and refund analytics. It does **not** currently support traffic-source, acquisition, or funnel analysis because `website_sessions.csv` and `website_pageviews.csv` are not present.

## 2. Target System Architecture

```text
Browser
  -> Next.js dashboard UI
      -> Spring Boot REST API
          -> DuckDB analytics layer
              -> CSV dataset files
```

### Architectural decisions

1. **Spring Boot remains the backend and final host**
   - APIs live under `/api/**`
   - final built frontend assets are served by Spring Boot so the finished app runs from one URL

2. **DuckDB is the analytics engine**
   - read CSVs into relational tables at startup
   - create typed analytical views for joins and aggregations
   - keep business logic in SQL + Java services instead of pushing analytics into the browser

3. **Next.js remains the frontend shell**
   - React Query fetches APIs
   - Zustand holds UI state such as global filters and selected comparison modes
   - Highcharts powers rich charts
   - TanStack Table powers detailed grids

4. **Dashboard-first UX**
   - every page begins with a summary
   - charts explain movement
   - insight cards narrate the meaning
   - tables prove the story with detail

## 3. Data Model

### Base tables

- `orders`
- `order_items`
- `order_item_refunds`
- `products`

### Analytical views to create

#### `vw_order_facts`
One row per order with:
- order metadata
- revenue
- cogs
- gross_profit
- gross_margin_pct
- refund_amount
- net_revenue
- order_month
- primary_product fields

#### `vw_order_item_facts`
One row per order item with:
- item revenue
- item cogs
- item gross profit
- product fields
- refund amount if refunded
- is_primary_item
- order_month

#### `vw_monthly_business_performance`
Monthly metrics:
- orders
- items_sold
- revenue
- cogs
- gross_profit
- gross_margin_pct
- refunds
- net_revenue
- avg_order_value

#### `vw_product_performance`
Per-product metrics:
- revenue
- units_sold
- gross_profit
- gross_margin_pct
- refund_amount
- refund_rate_pct
- share_of_revenue_pct

#### `vw_refund_performance`
Refund metrics by month and product:
- refunded_items
- refund_amount
- refund_rate_pct
- refund_share_pct

## 4. API Contract

### Global filters
Supported query parameters where relevant:
- `startDate`
- `endDate`
- `productId`
- `granularity=month|quarter|year`

### Endpoints

#### `GET /api/dashboard/summary`
Returns:
- revenue
- netRevenue
- orders
- itemsSold
- grossProfit
- grossMarginPct
- refunds
- refundRatePct
- avgOrderValue
- period-over-period deltas where possible

#### `GET /api/dashboard/sales-trend`
Returns time series for:
- revenue
- orders
- avgOrderValue
- netRevenue

#### `GET /api/dashboard/product-performance`
Returns:
- ranked products
- product revenue share
- product margin
- units sold
- refund impact

#### `GET /api/dashboard/profitability`
Returns:
- revenue vs cogs vs gross profit trend
- margin trend
- profit contribution by product

#### `GET /api/dashboard/refunds`
Returns:
- refund trend
- refund rate trend
- refunds by product
- highest-refund products

#### `GET /api/dashboard/orders`
Returns paginated detailed rows for the data grid:
- `page`
- `size`
- `sort`
- filter parameters

#### `GET /api/dashboard/metadata`
Returns:
- available products
- min/max date
- dataset freshness metadata

## 5. Frontend Information Architecture

### Route 1: `/` — Executive Overview
Sections:
1. Hero + reporting period
2. KPI strip
3. Revenue / net revenue trend
4. Product contribution chart
5. Margin health card
6. Refund alert card
7. “What changed?” narrative insights

### Route 2: `/sales`
Sections:
1. Sales KPI strip
2. Monthly revenue and orders chart
3. AOV trend
4. Top months / weak months
5. Supporting grid

### Route 3: `/products`
Sections:
1. Product contribution cards
2. Revenue share chart
3. Units sold chart
4. Margin vs revenue bubble/scatter
5. Product ranking grid

### Route 4: `/profitability`
Sections:
1. Profitability KPIs
2. Revenue vs COGS stacked/area chart
3. Margin trend
4. Gross profit by product
5. Interpretation panel

### Route 5: `/refunds`
Sections:
1. Refund KPIs
2. Refund amount trend
3. Refund rate by product
4. Most refund-prone products
5. Raw refund grid

### Route 6: `/explorer`
Sections:
1. Dataset selector
2. Advanced filters
3. Dense sortable table
4. CSV export

## 6. Visualization Plan

### Recommended Highcharts usage
- line/area: monthly trends
- stacked area or columns: revenue vs COGS vs gross profit
- donut: product revenue share
- bar: product ranking
- scatter/bubble: margin vs revenue by product
- heatmap optional later: monthly product performance

### Infographic components
- period headline
- delta badges
- best/worst product callouts
- “refund drag” card
- narrative milestone strip

### Data grids
- orders grid
- product performance grid
- refunds grid
- support sorting, search, filters, pagination, export

## 7. Delivery Backlog

### Epic A — Analytics Foundation
1. Add DuckDB dependency and config
2. Build startup CSV ingestion
3. Define typed base tables
4. Create analytical SQL views
5. Add repository/service layer
6. Add backend unit + integration tests

### Epic B — REST API Surface
1. Summary endpoint
2. Sales trend endpoint
3. Product performance endpoint
4. Profitability endpoint
5. Refunds endpoint
6. Orders grid endpoint
7. Metadata endpoint
8. API contract tests

### Epic C — Frontend Foundation
1. Add Highcharts dependencies
2. Build app shell and navigation
3. Add query client and API client layer
4. Add global filters store
5. Build reusable dashboard primitives:
   - KPI card
   - section header
   - insight card
   - chart shell
   - empty/loading/error states
6. Add visual tokens and responsive layout rules

### Epic D — Dashboard Pages
1. Executive Overview
2. Sales page
3. Product page
4. Profitability page
5. Refunds page
6. Explorer page

### Epic E — Quality and Delivery
1. Unit/component tests
2. API tests
3. Playwright end-to-end flows
4. Accessibility pass
5. Performance pass
6. Build frontend into Spring Boot static assets
7. Launch full app from Spring Boot and demo in browser

## 8. Recommended Build Order

```text
1. DuckDB ingestion + analytical views
2. Summary + metadata APIs
3. Frontend shell + shared components
4. Executive Overview page
5. Sales and Product pages
6. Profitability and Refunds pages
7. Explorer grid
8. Packaging into Spring Boot
9. Final polish and verification
```

This order gives us useful vertical slices early while keeping the analytical foundation coherent.

## 9. Testing Strategy

### Backend
- parser/loader tests
- repository query tests
- service calculation tests
- controller contract tests

### Frontend
- component tests
- chart config tests where practical
- state/filter tests
- transformation utility tests
- empty/loading/error state tests

### End-to-end
- landing dashboard loads
- global filter changes update charts
- navigation between groups works
- product page renders ranked data
- explorer grid paginates and filters
- final Spring Boot-hosted app loads successfully

Coverage target remains 90% minimum overall.

## 10. Definition of Done

A feature is done only when:
- backend endpoint exists
- frontend consumes live API data
- loading/error/empty states exist
- tests are written
- coverage stays at or above 90%
- Spring Boot launches cleanly
- browser validation passes
- the UI remains visually polished and story-driven

## 11. Immediate Next Sprint

### Sprint Goal
Deliver the first real vertical slice: **Executive Overview backed by live analytics**.

### Sprint scope
1. Add DuckDB
2. Load the four CSV datasets
3. Build the first analytical views
4. Implement `/api/dashboard/summary`
5. Implement `/api/dashboard/sales-trend`
6. Implement `/api/dashboard/metadata`
7. Replace placeholder frontend metrics with live API data
8. Add first real Highcharts revenue trend
9. Add tests and end-to-end verification

### Sprint outcome
When the root URL loads, the user should see a real executive dashboard powered by the dataset, not hardcoded placeholder values.
