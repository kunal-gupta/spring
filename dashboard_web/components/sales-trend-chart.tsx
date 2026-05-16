"use client";

import { Chart, Credits, Legend, Title, Tooltip, XAxis, YAxis } from "@highcharts/react";
import { LineSeries } from "@highcharts/react/series/Line";
import type { SalesTrendPoint } from "@/lib/api";

export function SalesTrendChart({ points }: { points: SalesTrendPoint[] }) {
  return (
    <div className="rounded-3xl border border-white/10 bg-white/5 p-5 shadow-2xl shadow-slate-950/30 backdrop-blur">
      <Chart>
        <Title>Revenue arc</Title>
        <XAxis categories={points.map((point) => point.month)} />
        <YAxis title={{ text: "USD" }} />
        <Tooltip shared />
        <Legend />
        <Credits enabled={false} />
        <LineSeries name="Revenue" data={points.map((point) => point.revenue)} />
        <LineSeries name="Net revenue" data={points.map((point) => point.netRevenue)} />
      </Chart>
    </div>
  );
}
