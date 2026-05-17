# BI Dashboard (Maven Fuzzy Factory)

## Purpose of the Project
This project is a rich, story-driven Business Intelligence (BI) application built over the Maven Fuzzy Factory commerce dataset. It is designed to answer key executive-level questions regarding revenue generation, business growth, product profitability, margin health, and the impact of refunds. Rather than simply displaying raw metrics, the application guides the viewer through a narrative, surfacing the most important business movements first and using strong visual hierarchy to make data interpretation intuitive.

## Tech Stack Used

### Backend
*   **Java 17** & **Spring Boot 4.0.6**: Serves as the core backend and REST API layer, as well as the final host for the built frontend assets.
*   **DuckDB 1.5.2**: An in-process SQL analytics engine used to read the raw dataset CSVs, create typed analytical views, and execute complex joins and aggregations rapidly.
*   **Maven**: Dependency management and build tool (configured to build both backend and frontend).

### Frontend
*   **Next.js 16.2**: The React framework used for the application shell and routing.
*   **React 19.2**: UI library for building the dashboard components.
*   **Tailwind CSS 4.3**: Utility-first CSS framework for styling.
*   **Zustand**: Client-side state management for handling global filters and comparison modes.
*   **TanStack Query (React Query)**: Used for data fetching and caching server state.
*   **Highcharts & Recharts**: Utilized for rich, interactive, and complex charting visualizations.
*   **TanStack Table**: Powers the detailed, sortable data grids.

### Testing
*   **Vitest & React Testing Library**: For frontend unit and component testing.
*   **Playwright**: For comprehensive end-to-end browser testing.
*   **JUnit & Spring Boot Test**: For backend parser, repository, and controller testing.

## Search Terms & References
To learn more about the concepts, data, and technologies used in this project, you can refer to the following Google search terms:
*   "Maven Fuzzy Factory dataset analysis"
*   "Embedded analytics with DuckDB in Java"
*   "Spring Boot REST API with DuckDB JDBC"
*   "Next.js BI Dashboard architecture"
*   "Highcharts React integration examples"
*   "Story-driven Business Intelligence dashboards design"
*   "TanStack React Query data fetching best practices"
*   "Zustand state management for global filters"

## Example: How the Dashboard Should Look
The dashboard is designed with a dashboard-first UX, meaning every page begins with a summary and drills down into specifics. A typical **Executive Overview** looks like this:

1.  **Hero & Reporting Period**: A clear title indicating the date range being analyzed.
2.  **KPI Strip**: A row of high-level metric cards showing Total Revenue, Net Revenue, Orders, Gross Margin, and Refund Rate, complete with period-over-period delta badges (e.g., "+5% vs last month").
3.  **Revenue Trend Chart**: A large area or line chart showing monthly revenue and net revenue trends to quickly visualize business growth.
4.  **Product Contribution**: A donut chart or ranked bar chart illustrating which specific products are driving the most revenue.
5.  **Insight Cards (Margin & Refunds)**: Narrative "What changed?" cards that explain the data, such as highlighting a "refund drag" caused by a specific product.
6.  **Data Grids**: Dense, sortable tables at the bottom of the view that provide the granular row-level data proving the high-level story.

The visual experience relies on restrained color palettes (using colors semantically to highlight good/bad movements) and thoughtful whitespace to avoid clutter.
