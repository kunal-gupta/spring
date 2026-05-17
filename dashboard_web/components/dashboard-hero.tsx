"use client";

import { useQuery } from "@tanstack/react-query";
import { dashboardApi } from "@/lib/api";
import { SalesTrendChart } from "@/components/sales-trend-chart";
import {
  BarChart3,
  Boxes,
  CircleDollarSign,
  PanelLeftClose,
  PanelLeftOpen,
  ReceiptText,
  RefreshCcw
} from "lucide-react";
import { useState } from "react";

type AnalysisTab = "overview" | "sales" | "products" | "profitability" | "refunds" | "gridStudio" | "storyboard";

function formatCurrency(value: number) {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    maximumFractionDigits: 0
  }).format(value);
}

function formatCompact(value: number) {
  return new Intl.NumberFormat("en-US", {
    notation: "compact",
    maximumFractionDigits: 1
  }).format(value);
}

export function DashboardHero() {
  const summary = useQuery({ queryKey: ["summary"], queryFn: dashboardApi.summary });
  const trend = useQuery({ queryKey: ["sales-trend"], queryFn: dashboardApi.salesTrend });
  const metadata = useQuery({ queryKey: ["metadata"], queryFn: dashboardApi.metadata });
  const [activeTab, setActiveTab] = useState<AnalysisTab>("gridStudio");
  const [isDocked, setIsDocked] = useState(true);

  if (summary.isLoading || trend.isLoading || metadata.isLoading) {
    return <main className="min-h-screen px-6 py-8 text-slate-100">Loading dashboard...</main>;
  }

  if (summary.isError || trend.isError || metadata.isError || !summary.data || !trend.data || !metadata.data) {
    return <main className="min-h-screen px-6 py-8 text-slate-100">Unable to load dashboard data.</main>;
  }

  const heroMetrics = [
    { label: "Revenue", value: formatCurrency(summary.data.revenue), detail: "Commercial scale" },
    { label: "Orders", value: formatCompact(summary.data.orders), detail: "Demand signal" },
    { label: "Gross margin", value: `${summary.data.grossMarginPct}%`, detail: "Profit quality" },
    { label: "Refunds", value: formatCurrency(summary.data.refunds), detail: `${summary.data.refundRatePct}% revenue drag` }
  ];

  const tabs = [
    { id: "gridStudio" as const, label: "Sales Insight", icon: Boxes },
    { id: "overview" as const, label: "Executive Overview", icon: BarChart3 },
    { id: "sales" as const, label: "Sales Performance", icon: CircleDollarSign },
    { id: "products" as const, label: "Product Intelligence", icon: Boxes },
    { id: "profitability" as const, label: "Profitability", icon: ReceiptText },
    { id: "refunds" as const, label: "Refunds & Quality", icon: RefreshCcw },
    { id: "storyboard" as const, label: "Storyboard", icon: BarChart3 }
  ];

  const analysisContent = {
    overview: (
      <>
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
          {heroMetrics.map((metric) => (
            <article
              key={metric.label}
              className="rounded-3xl border border-white/10 bg-white/5 p-5 shadow-2xl shadow-cyan-950/20 backdrop-blur"
            >
              <p className="text-sm text-slate-400">{metric.label}</p>
              <p className="mt-3 text-3xl font-semibold">{metric.value}</p>
              <p className="mt-2 text-sm text-cyan-200">{metric.detail}</p>
            </article>
          ))}
        </div>
        <SalesTrendChart points={trend.data} />
      </>
    ),
    sales: (
      <>
        <div className="grid gap-4 md:grid-cols-3">
          <MetricCard label="Orders" value={formatCompact(summary.data.orders)} detail="Demand volume" />
          <MetricCard label="Average order value" value={formatCurrency(summary.data.avgOrderValue)} detail="Basket strength" />
          <MetricCard label="Items sold" value={formatCompact(summary.data.itemsSold)} detail="Units moved" />
        </div>
        <SalesTrendChart points={trend.data} />
      </>
    ),
    products: (
      <InsightPanel
        title="Product intelligence"
        body={`The catalog contains ${metadata.data.products.length} products. Next is contribution, volume, and margin by product.`}
      />
    ),
    profitability: (
      <>
        <div className="grid gap-4 md:grid-cols-3">
          <MetricCard label="Gross profit" value={formatCurrency(summary.data.grossProfit)} detail="Revenue after COGS" />
          <MetricCard label="Gross margin" value={`${summary.data.grossMarginPct}%`} detail="Profit quality" />
          <MetricCard label="Net revenue" value={formatCurrency(summary.data.netRevenue)} detail="After refunds" />
        </div>
        <InsightPanel
          title="Profitability lens"
          body="This view separates scale from quality: revenue is momentum, margin is value."
        />
      </>
    ),
    refunds: (
      <>
        <div className="grid gap-4 md:grid-cols-3">
          <MetricCard label="Refunds" value={formatCurrency(summary.data.refunds)} detail="Value returned" />
          <MetricCard label="Refund rate" value={`${summary.data.refundRatePct}%`} detail="Revenue drag" />
          <MetricCard label="Net revenue" value={formatCurrency(summary.data.netRevenue)} detail="Retained value" />
        </div>
        <InsightPanel
          title="Refund pressure"
          body="Refunds are small, but significant enough to justify close monitoring."
        />
      </>
    ),
    gridStudio: (
      <>
        <section className="rounded-[2rem] border border-cyan-300/20 bg-cyan-300/[0.05] p-5 shadow-2xl shadow-cyan-950/20">
          <div className="mb-5 flex flex-col justify-between gap-4 md:flex-row md:items-end">
            <div>
              <p className="text-sm uppercase tracking-[0.22em] text-cyan-300">Scorecard</p>
              <h2 className="mt-2 text-2xl font-semibold">Business scorecard</h2>
            </div>
            <div className="flex flex-wrap gap-2 text-xs">
              <span className="rounded-full border border-cyan-200/20 bg-cyan-200/10 px-3 py-1 text-cyan-100">Trend group</span>
              <span className="rounded-full border border-cyan-200/20 bg-cyan-200/10 px-3 py-1 text-cyan-100">KPI tiles</span>
              <span className="rounded-full border border-cyan-200/20 bg-cyan-200/10 px-3 py-1 text-cyan-100">Summary grid</span>
            </div>
          </div>

          <div className="grid gap-4 lg:grid-cols-[1.05fr_1fr]">
            <div className="rounded-[1.6rem] border border-white/10 bg-slate-950/20 p-4">
              <p className="mb-2 text-xs uppercase tracking-[0.22em] text-slate-400">Trend summary</p>
              <p className="mb-4 text-sm text-slate-300">Revenue momentum over the last 12 periods.</p>
              <SalesTrendChart points={trend.data.slice(-12)} />
            </div>

            <div className="rounded-[1.6rem] border border-white/10 bg-slate-950/20 p-4">
              <p className="mb-2 text-xs uppercase tracking-[0.22em] text-slate-400">KPI tiles</p>
              <p className="mb-4 text-sm text-slate-300">Six core metrics for a quick read.</p>
              <div className="grid gap-4 sm:grid-cols-2">
                <MetricCard label="Revenue" value={formatCurrency(summary.data.revenue)} detail="Scale" indicatorColor="green" />
                <MetricCard label="Orders" value={formatCompact(summary.data.orders)} detail="Demand" indicatorColor="green" />
                <MetricCard label="Gross profit" value={formatCurrency(summary.data.grossProfit)} detail="Value" indicatorColor="yellow" />
                <MetricCard label="Gross margin" value={`${summary.data.grossMarginPct}%`} detail="Efficiency" indicatorColor="yellow" />
                <MetricCard label="Refunds" value={formatCurrency(summary.data.refunds)} detail="Leakage" indicatorColor="red" />
                <MetricCard label="Net revenue" value={formatCurrency(summary.data.netRevenue)} detail="Outcome" indicatorColor="green" />
              </div>
            </div>
          </div>
        </section>

        <section className="rounded-[2rem] border border-white/10 bg-white/[0.03] p-5 shadow-2xl shadow-slate-950/20">
          <p className="mb-2 text-xs uppercase tracking-[0.22em] text-slate-400">Summary</p>
          <p className="mb-4 text-sm text-slate-300">High-level KPI signals for rhythm and trend.</p>
          <SummaryGrid
            rows={[
              ["Revenue", formatCurrency(summary.data.revenue), "Scale"],
              ["Net revenue", formatCurrency(summary.data.netRevenue), "Retained value"],
              ["Average order value", formatCurrency(summary.data.avgOrderValue), "Basket strength"],
              ["Refund rate", `${summary.data.refundRatePct}%`, "Quality signal"]
            ]}
          />
        </section>
      </>
    ),
    storyboard: (
      <>
        <section className="overflow-hidden rounded-[2rem] border border-white/10 bg-gradient-to-br from-cyan-300/15 via-white/[0.04] to-fuchsia-300/10 p-6 shadow-2xl shadow-cyan-950/20">
          <div className="grid gap-6 lg:grid-cols-[1fr_0.9fr]">
            <div>
              <p className="text-sm uppercase tracking-[0.25em] text-cyan-300">More promising layout</p>
              <h2 className="mt-3 max-w-xl text-4xl font-semibold leading-tight">
                Growth is strongest when revenue, margin, and retention move together.
              </h2>
              <p className="mt-5 max-w-xl leading-7 text-slate-300">
                This composition leads with the verdict, not the widgets. The supporting modules then prove the story with a deliberate hierarchy.
              </p>
            </div>
            <div className="grid gap-4 sm:grid-cols-2">
              <MetricCard label="Revenue" value={formatCurrency(summary.data.revenue)} detail="Top-line momentum" />
              <MetricCard label="Gross profit" value={formatCurrency(summary.data.grossProfit)} detail="Value created" />
              <MetricCard label="Orders" value={formatCompact(summary.data.orders)} detail="Demand engine" />
              <MetricCard label="Refund drag" value={`${summary.data.refundRatePct}%`} detail="Leakage watch" />
            </div>
          </div>
        </section>

        <div className="grid gap-6 lg:grid-cols-[1.2fr_0.8fr]">
          <SalesTrendChart points={trend.data} />
          <article className="rounded-3xl border border-white/10 bg-white/5 p-6 shadow-2xl shadow-slate-950/30 backdrop-blur">
            <p className="text-sm uppercase tracking-[0.22em] text-cyan-300">Narrative stack</p>
            <div className="mt-5 space-y-4">
              <StoryBeat title="1. Scale" body={`${formatCurrency(summary.data.revenue)} in revenue establishes the business frame.`} />
              <StoryBeat title="2. Quality" body={`${summary.data.grossMarginPct}% gross margin shows that growth keeps its value.`} />
              <StoryBeat title="3. Leakage" body={`${formatCurrency(summary.data.refunds)} in refunds becomes the question worth drilling into next.`} />
            </div>
          </article>
        </div>

        <SummaryGrid
          rows={[
            ["Orders", formatCompact(summary.data.orders), "Demand"],
            ["Items sold", formatCompact(summary.data.itemsSold), "Volume"],
            ["Gross margin", `${summary.data.grossMarginPct}%`, "Efficiency"],
            ["Net revenue", formatCurrency(summary.data.netRevenue), "Outcome"]
          ]}
        />
      </>
    )
  };

  return (
    <main className="h-screen overflow-hidden px-4 py-4 text-slate-100 md:px-6 lg:px-8">
      <section className="flex h-full w-full gap-5">
        <aside
          className={`flex shrink-0 flex-col overflow-y-auto rounded-[2rem] border border-white/10 bg-slate-950/60 p-4 shadow-2xl shadow-slate-950/30 backdrop-blur transition-all ${
            isDocked ? "w-72" : "w-20"
          }`}
          aria-label="Analysis navigation"
        >
          <div className="mb-6 flex items-center justify-between gap-3">
            {isDocked ? (
              <div>
                <p className="text-sm uppercase tracking-[0.25em] text-cyan-300">BI Dashboard</p>
                <p className="mt-1 text-xs text-slate-400">Analysis groups</p>
              </div>
            ) : null}
            <button
              aria-label={isDocked ? "Undock sidebar" : "Dock sidebar"}
              className="cursor-pointer rounded-2xl border border-white/10 p-2 text-slate-300 transition hover:bg-white/10"
              onClick={() => setIsDocked((current) => !current)}
              type="button"
            >
              {isDocked ? <PanelLeftClose size={18} /> : <PanelLeftOpen size={18} />}
            </button>
          </div>

          <nav role="tablist" aria-label="Dashboard analyses" className="space-y-2">
            {tabs.map((tab) => {
              const Icon = tab.icon;
              const selected = activeTab === tab.id;
              return (
                <button
                  key={tab.id}
                  aria-selected={selected}
                  className={`flex w-full cursor-pointer items-center gap-3 rounded-2xl px-3 py-3 text-left transition ${
                    selected ? "bg-cyan-400 text-slate-950" : "text-slate-300 hover:bg-white/10"
                  }`}
                  onClick={() => setActiveTab(tab.id)}
                  role="tab"
                  type="button"
                >
                  <Icon size={18} />
                  {isDocked ? <span className="text-sm font-medium">{tab.label}</span> : null}
                </button>
              );
            })}
          </nav>
        </aside>

        <div className="min-w-0 flex-1 overflow-y-auto rounded-[2rem] border border-white/10 bg-white/[0.03] p-5 shadow-2xl shadow-slate-950/20 backdrop-blur md:p-8">
        <div className="max-w-3xl">
          <p className="mb-3 text-sm uppercase tracking-[0.3em] text-cyan-300">Maven Fuzzy Factory</p>
          <h1 className="text-4xl font-semibold tracking-tight md:text-6xl">
            E-Commerce Analytics
          </h1>
          <p className="mt-5 max-w-2xl text-base leading-7 text-slate-300 md:text-lg">
            Start with the health of the business, then reveal the forces underneath: product mix, profitability, and refund pressure.
          </p>
          <p className="mt-4 text-sm text-slate-400">
            Reporting period: {metadata.data.minDate} to {metadata.data.maxDate}
          </p>
        </div>

          <div className="mt-8 flex flex-col gap-6" role="tabpanel">
            {analysisContent[activeTab]}
          </div>
        </div>
      </section>
    </main>
  );
}

function MetricCard({ label, value, detail, indicatorColor }: { label: string; value: string; detail: string; indicatorColor?: "green" | "red" | "yellow" }) {
  const colorClasses = {
    green: "border-l-[10px] border-l-emerald-500/40 border-y border-r border-y-white/10 border-r-white/10",
    red: "border-l-[10px] border-l-rose-500/40 border-y border-r border-y-white/10 border-r-white/10",
    yellow: "border-l-[10px] border-l-amber-500/40 border-y border-r border-y-white/10 border-r-white/10",
  };

  const borderClass = indicatorColor ? colorClasses[indicatorColor] : "border border-white/10";

  return (
    <article className={`rounded-3xl bg-white/5 p-5 shadow-2xl shadow-cyan-950/20 backdrop-blur ${borderClass}`}>
      <p className="text-sm text-slate-400">{label}</p>
      <p className="mt-3 text-3xl font-semibold">{value}</p>
      <p className="mt-2 text-sm text-cyan-200">{detail}</p>
    </article>
  );
}

function InsightPanel({ title, body }: { title: string; body: string }) {
  return (
    <article className="rounded-3xl border border-white/10 bg-white/5 p-6 shadow-2xl shadow-slate-950/30 backdrop-blur">
      <p className="text-sm uppercase tracking-[0.22em] text-cyan-300">Insight</p>
      <h2 className="mt-3 text-2xl font-semibold">{title}</h2>
      <p className="mt-4 max-w-3xl leading-7 text-slate-300">{body}</p>
    </article>
  );
}

function SummaryGrid({ rows }: { rows: Array<[string, string, string]> }) {
  return (
    <section className="overflow-hidden rounded-3xl border border-white/10 bg-white/5 shadow-2xl shadow-slate-950/20 backdrop-blur">
      <div className="grid grid-cols-[1.2fr_1fr_1fr] border-b border-white/10 px-5 py-3 text-xs uppercase tracking-[0.2em] text-slate-400">
        <span>Metric</span>
        <span>Value</span>
        <span>Signal</span>
      </div>
      {rows.map(([metric, value, signal]) => (
        <div key={metric} className="grid grid-cols-[1.2fr_1fr_1fr] border-b border-white/5 px-5 py-4 last:border-b-0">
          <span className="text-slate-300">{metric}</span>
          <span className="font-medium text-white">{value}</span>
          <span className="text-cyan-200">{signal}</span>
        </div>
      ))}
    </section>
  );
}

function StoryBeat({ title, body }: { title: string; body: string }) {
  return (
    <div className="rounded-2xl border border-white/10 bg-slate-950/30 p-4">
      <p className="font-medium text-white">{title}</p>
      <p className="mt-2 text-sm leading-6 text-slate-300">{body}</p>
    </div>
  );
}

