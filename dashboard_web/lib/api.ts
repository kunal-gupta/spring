export type DashboardSummary = {
  revenue: number;
  netRevenue: number;
  orders: number;
  itemsSold: number;
  grossProfit: number;
  grossMarginPct: number;
  refunds: number;
  refundRatePct: number;
  avgOrderValue: number;
};

export type SalesTrendPoint = {
  month: string;
  revenue: number;
  netRevenue: number;
  orders: number;
  avgOrderValue: number;
};

export type DashboardMetadata = {
  minDate: string;
  maxDate: string;
  products: Array<{ productId: number; productName: string }>;
};

async function getJson<T>(path: string): Promise<T> {
  const response = await fetch(path);

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`);
  }

  return response.json() as Promise<T>;
}

export const dashboardApi = {
  summary: () => getJson<DashboardSummary>("/api/dashboard/summary"),
  salesTrend: () => getJson<SalesTrendPoint[]>("/api/dashboard/sales-trend"),
  metadata: () => getJson<DashboardMetadata>("/api/dashboard/metadata")
};
