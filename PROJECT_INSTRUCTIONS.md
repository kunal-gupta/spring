# Project Instructions

This file is the standing reference for future work on the BI Dashboard project. Before making design, architecture, or implementation decisions, read this file and keep its guidance in scope.

## Product Aim

Build a polished, story-driven BI dashboard on top of the Maven Fuzzy Factory dataset. The application should not feel like a spreadsheet with charts attached; it should guide the viewer through a narrative, surface the most important business movements first, and use strong visual hierarchy, restrained color, thoughtful spacing, and elegant interactions throughout.

## Required Frontend Stack

Use the pinned versions below unless this file is intentionally revised:

| Purpose | Library | Version |
| --- | --- | --- |
| App framework | Next.js | 16.2.6 |
| UI library | React | 19.2.6 |
| DOM renderer | React DOM | 19.2.6 |
| Styling | Tailwind CSS | 4.3.0 |
| Client state | Zustand | 5.0.13 |
| Server/cache state | @tanstack/react-query | 5.100.10 |
| Data tables | @tanstack/react-table | 8.21.3 |
| Charts | Recharts | 3.8.1 |
| Rich charting | Highcharts | 12.6.0 |
| Highcharts React integration | @highcharts/react | 5.0.1 |
| Advanced visualizations | D3 | 7.9.0 |
| Icons | lucide-react | 1.16.0 |
| Dates | date-fns | 4.1.0 |
| Class helpers | clsx | 2.1.1 |
| Tailwind class merging | tailwind-merge | 3.6.0 |
| Schema validation | zod | 4.4.3 |
| Forms | react-hook-form | 7.76.0 |


### Supporting Frontend Tooling

| Purpose | Library | Version |
| --- | --- | --- |
| Tailwind PostCSS bridge | @tailwindcss/postcss | 4.3.0 |
| PostCSS | postcss | 8.5.14 |
| CSS prefixing | autoprefixer | 10.5.0 |
| TypeScript | typescript | 6.0.3 |
| Vite React plugin | @vitejs/plugin-react | 6.0.2 |
| Coverage provider | @vitest/coverage-v8 | 4.1.6 |
## Testing Standard

Every meaningful feature must include automated tests. Maintain at least 90% code coverage overall, with tests covering:

- rendering and interaction behavior for UI components
- state transitions and selectors
- data transformation and metric calculations
- empty, loading, and error states
- the most important end-to-end dashboard flows

Recommended pinned test stack:

| Purpose | Library | Version |
| --- | --- | --- |
| Unit/component tests | Vitest | 4.1.6 |
| React testing utilities | @testing-library/react | 16.3.2 |
| DOM assertions | @testing-library/jest-dom | 6.9.1 |
| Browser-like test environment | jsdom | 29.1.1 |
| End-to-end tests | Playwright | 1.60.0 |

## Post-Change Verification Rule

After making any code or configuration change in this repository:

1. run the relevant automated test suite
2. verify that coverage remains at or above 90%
3. start the Spring Boot application and confirm it launches successfully
4. validate the user-facing experience in a browser with Playwright MCP when browser-checkable behavior exists
5. treat visual quality as a release criterion: the dashboard should remain aesthetically excellent, not merely functional

## Dataset Location

Dataset files are stored under:

`bi_dashboard/src/main/resources/dataset/Maven+Fuzzy+Factory/`

### Files currently present

#### `orders.csv`
Represents one row per order.

Columns:
- `order_id` - primary key
- `created_at`
- `website_session_id`
- `user_id`
- `primary_product_id`
- `items_purchased`
- `price_usd`
- `cogs_usd`

#### `order_items.csv`
Represents one row per item sold within an order.

Columns:
- `order_item_id` - primary key
- `created_at`
- `order_id`
- `product_id`
- `is_primary_item`
- `price_usd`
- `cogs_usd`

#### `order_item_refunds.csv`
Represents one row per refunded order item.

Columns:
- `order_item_refund_id` - primary key
- `created_at`
- `order_item_id`
- `order_id`
- `refund_amount_usd`

#### `products.csv`
Represents the product dimension.

Columns:
- `product_id` - primary key
- `created_at`
- `product_name`

#### `maven_fuzzy_factory_data_dictionary.csv`
Reference dictionary describing all known tables and columns in the broader Maven Fuzzy Factory model.

### Relationships between files currently present

```text
products.product_id
        ^
        |
order_items.product_id

orders.order_id -----------------> order_items.order_id
orders.order_id -----------------> order_item_refunds.order_id
order_items.order_item_id ------> order_item_refunds.order_item_id
orders.primary_product_id ------> products.product_id
```

Cardinality:
- one order can have many order items
- one order item can have zero or more refund records
- one product can appear in many order items
- one order has one primary product reference

### Important dataset note

The data dictionary also describes `website_sessions` and `website_pageviews`, but those CSV files are not currently present in the dataset folder. Any future dashboard work that depends on acquisition, traffic source, funnel, or pageview behavior should first confirm whether those files will be added.

## Dashboard Design Principles

Always preserve the product intent:

- tell a business story, not just display metrics
- lead with executive-level KPIs, then explain drivers and detail
- use charts only where they clarify movement, contrast, composition, or causality
- prefer coherent page rhythm, refined typography, and whitespace over visual clutter
- make color semantic and sparing; reserve emphasis for what matters most
- build for curiosity: let each section answer one question and naturally invite the next
- keep the experience visually excellent on first load, in empty states, and at smaller screen sizes

## Working Rule for Future Prompts

For all future work in this repository:
1. read this file first
2. keep the pinned versions unless deliberately updating this document
3. write tests alongside implementation
4. protect the 90% coverage standard
5. run tests and launch the Spring Boot application after every change
6. use Playwright MCP for browser validation whenever the change can be checked in the UI
7. treat aesthetics and storytelling as core requirements, not polish work

