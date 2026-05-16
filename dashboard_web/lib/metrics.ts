export function calculateGrossMargin(revenue: number, cogs: number): number {
  if (revenue === 0) {
    return 0;
  }

  return Number((((revenue - cogs) / revenue) * 100).toFixed(1));
}
