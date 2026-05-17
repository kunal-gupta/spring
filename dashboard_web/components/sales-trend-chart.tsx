"use client";

import Highcharts from "highcharts";
import { Chart, Credits, Legend, Title, Tooltip, XAxis, YAxis } from "@highcharts/react";
import { LineSeries } from "@highcharts/react/series/Line";
import type { SalesTrendPoint } from "@/lib/api";

// Apply a dark theme tuned to the product palette
Highcharts.setOptions({
  chart: {
    backgroundColor: "transparent",
    style: {
      fontFamily: "Inter, ui-sans-serif, system-ui, -apple-system, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial",
      color: "#cbd5e1",
    },
  },
  title: { style: { color: "#f1f5f9", fontWeight: "700", fontSize: "18px" } },
  xAxis: {
    labels: { style: { color: "#94a3b8" } },
    lineColor: "#0f172a",
    tickColor: "#0f172a",
    gridLineColor: "#11182733",
  },
  yAxis: {
    labels: { style: { color: "#94a3b8" } },
    gridLineColor: "#0f172a33",
    title: { style: { color: "#94a3b8" } },
  },
  legend: { itemStyle: { color: "#cbd5e1" }, itemHoverStyle: { color: "#ffffff" } },
  tooltip: { backgroundColor: "rgba(2,6,23,0.85)", style: { color: "#e6eef8" } },
  colors: ["#22d3ee", "#8b5cf6"],
});

export function SalesTrendChart({ points }: { points: SalesTrendPoint[] }) {
  const categories = points.map((point) => point.month);
  const revenue = points.map((point) => point.revenue);
  const netRevenue = points.map((point) => point.netRevenue);

  return (
    <div className="rounded-3xl border border-white/10 bg-slate-950/20 p-5 shadow-2xl shadow-slate-950/30 backdrop-blur">
      <Chart
        containerProps={{ style: { width: "100%", height: 300 } }}
        chart={{}}
      >
        <Title>Revenue arc</Title>
        <XAxis categories={categories} />
        <YAxis title={{ text: "USD" }} />
        <Tooltip shared />
        <Legend />
        <Credits enabled={false} />
        <LineSeries
          name="Revenue"
          data={revenue}
          marker={{ enabled: true, radius: 3 }}
          lineWidth={3}
          states={{ hover: { lineWidth: 4 } }}
        />
        <LineSeries
          name="Net revenue"
          data={netRevenue}
          marker={{ enabled: true, radius: 2 }}
          dashStyle="ShortDash"
          lineWidth={2}
          states={{ hover: { lineWidth: 3 } }}
        />
      </Chart>
    </div>
  );
}
