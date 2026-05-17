import { render, screen } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { DashboardHero } from "@/components/dashboard-hero";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { fireEvent } from "@testing-library/react";

function renderWithClient(ui: React.ReactNode) {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: { retry: false }
    }
  });

  return render(<QueryClientProvider client={queryClient}>{ui}</QueryClientProvider>);
}

beforeEach(() => {
  vi.stubGlobal("fetch", vi.fn((url: string) => {
    const body =
      url === "/api/dashboard/summary"
        ? {
            revenue: 100000,
            netRevenue: 95000,
            orders: 1200,
            itemsSold: 1400,
            grossProfit: 60000,
            grossMarginPct: 60,
            refunds: 5000,
            refundRatePct: 5,
            avgOrderValue: 83.33
          }
        : url === "/api/dashboard/sales-trend"
          ? [{ month: "2012-03", revenue: 100000, netRevenue: 95000, orders: 1200, avgOrderValue: 83.33 }]
          : {
              minDate: "2012-03-19",
              maxDate: "2015-03-31",
              products: []
            };

    return Promise.resolve(new Response(JSON.stringify(body), { status: 200 }));
  }) as typeof fetch);
});

describe("DashboardHero", () => {
  it("frames the dashboard as a story", async () => {
    renderWithClient(<DashboardHero />);

    expect(await screen.findByRole("heading", { name: /e-commerce analytics/i })).toBeInTheDocument();
    expect(screen.getByText(/maven fuzzy factory/i)).toBeInTheDocument();
  });

  it("shows live headline metrics", async () => {
    renderWithClient(<DashboardHero />);

    expect((await screen.findAllByText("Revenue")).length).toBeGreaterThan(0);
    expect(screen.getByText("Orders")).toBeInTheDocument();
    expect(screen.getByText("Gross margin")).toBeInTheDocument();
    expect(screen.getAllByText("$100,000").length).toBeGreaterThan(0);
    expect(screen.getByText("60%")).toBeInTheDocument();
  });

  it("shows a loading state before data arrives", () => {
    vi.stubGlobal("fetch", vi.fn(() => new Promise(() => undefined)) as typeof fetch);

    renderWithClient(<DashboardHero />);

    expect(screen.getByText(/loading dashboard/i)).toBeInTheDocument();
  });

  it("shows an error state when data loading fails", async () => {
    vi.stubGlobal("fetch", vi.fn(() => Promise.resolve(new Response("", { status: 500 }))) as typeof fetch);

    renderWithClient(<DashboardHero />);

    expect(await screen.findByText(/unable to load dashboard data/i)).toBeInTheDocument();
  });

  it("switches between analysis tabs", async () => {
    renderWithClient(<DashboardHero />);

    await screen.findAllByText("Revenue");
    fireEvent.click(screen.getByRole("tab", { name: /^profitability$/i }));

    expect(screen.getByText(/profitability lens/i)).toBeInTheDocument();
    expect(screen.getByText("Gross profit")).toBeInTheDocument();
  });

  it("can collapse and expand the sidebar", async () => {
    renderWithClient(<DashboardHero />);

    await screen.findAllByText("Revenue");
    screen.getByRole("button", { name: /undock sidebar/i }).click();

    expect(screen.getByRole("button", { name: /dock sidebar/i })).toBeInTheDocument();
  });

  it("shows the grouped KPI studio layout", async () => {
    renderWithClient(<DashboardHero />);

    await screen.findAllByText("Revenue");
    fireEvent.click(screen.getByRole("tab", { name: /sales insight/i }));

    expect(screen.getByText(/business scorecard/i)).toBeInTheDocument();
    expect(screen.getByText(/trend summary/i)).toBeInTheDocument();
    expect(screen.getAllByText(/kpi tiles/i).length).toBeGreaterThan(0);
    expect(screen.getAllByText(/summary grid/i).length).toBeGreaterThan(0);
    expect(screen.getByText("Metric")).toBeInTheDocument();
  });
  it("shows the richer storyboard layout", async () => {
    renderWithClient(<DashboardHero />);

    await screen.findAllByText("Revenue");
    fireEvent.click(screen.getByRole("tab", { name: /storyboard/i }));

    expect(screen.getByText(/more promising layout/i)).toBeInTheDocument();
    expect(screen.getByText(/growth is strongest when revenue, margin, and retention move together/i)).toBeInTheDocument();
    expect(screen.getByText(/narrative stack/i)).toBeInTheDocument();
  });
});
