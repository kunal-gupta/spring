import React from "react";
import { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { apiRequest } from "../api";

function formatDate(dateTimeValue) {
  if (!dateTimeValue) return "-";
  const parsed = new Date(dateTimeValue);
  return Number.isNaN(parsed.getTime()) ? dateTimeValue : parsed.toLocaleString();
}

export default function VisitWorkspacePage() {
  const location = useLocation();
  const navigate = useNavigate();
  const state = location.state || {};

  const initialForm = useMemo(
    () => ({
      name: state.visitorSummary?.name || "",
      address: state.visitorSummary?.address || "",
      phone: state.lookupPhone || state.visitorSummary?.phone || "",
      email: state.lookupEmail || state.visitorSummary?.email || "",
      designation: state.visitorSummary?.designation || "",
      purpose: "",
      notes: ""
    }),
    [state.lookupEmail, state.lookupPhone, state.visitorSummary]
  );

  const [form, setForm] = useState(initialForm);
  const [visitorSummary, setVisitorSummary] = useState(state.visitorSummary || null);
  const [history, setHistory] = useState([]);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!state.lookupPhone && !state.lookupEmail && !state.visitorSummary) {
      navigate("/visit", { replace: true });
    }
  }, [navigate, state.lookupEmail, state.lookupPhone, state.visitorSummary]);

  useEffect(() => {
    async function loadHistory() {
      if (!visitorSummary?.id) {
        setHistory([]);
        return;
      }
      try {
        const items = await apiRequest(`/api/visitors/${visitorSummary.id}/history`);
        setHistory(items || []);
      } catch {
        setHistory([]);
      }
    }
    loadHistory();
  }, [visitorSummary?.id]);

  function updateField(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setError("");
    setMessage("");

    try {
      const createdVisit = await apiRequest("/api/visitors/visits", {
        method: "POST",
        body: JSON.stringify(form)
      });

      setMessage(`Visit logged for ${createdVisit.visitorName}.`);
      const query = new URLSearchParams();
      if (form.phone) query.set("phone", form.phone);
      if (form.email) query.set("email", form.email);

      if (query.toString()) {
        const summary = await apiRequest(`/api/visitors/search?${query.toString()}`);
        setVisitorSummary(summary);
      }

      setForm((prev) => ({ ...prev, purpose: "", notes: "" }));
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <section className="workspace">
      <div className="panel">
        <h2>Log Visit Entry</h2>
        <p className="muted">Left side is for new log entry. Required field from backend: name.</p>

        <form onSubmit={handleSubmit} className="form-grid two-col">
          <label>
            Name *
            <input name="name" value={form.name} onChange={updateField} required />
          </label>
          <label>
            Designation
            <input name="designation" value={form.designation} onChange={updateField} />
          </label>
          <label>
            Mobile
            <input name="phone" value={form.phone} onChange={updateField} />
          </label>
          <label>
            Email
            <input name="email" value={form.email} onChange={updateField} type="email" />
          </label>
          <label className="full-width">
            Address
            <input name="address" value={form.address} onChange={updateField} />
          </label>
          <label className="full-width">
            Purpose
            <input name="purpose" value={form.purpose} onChange={updateField} />
          </label>
          <label className="full-width">
            Notes
            <textarea name="notes" value={form.notes} onChange={updateField} rows={4} />
          </label>
          {error && <p className="error full-width">{error}</p>}
          {message && <p className="success full-width">{message}</p>}
          <button className="full-width" type="submit" disabled={saving}>
            {saving ? "Saving..." : "Save Visit"}
          </button>
        </form>
      </div>

      <aside className="panel history-panel">
        <h2>Visit History</h2>
        {visitorSummary ? (
          <div className="summary-card">
            <p><strong>Name:</strong> {visitorSummary.name}</p>
            <p><strong>Phone:</strong> {visitorSummary.phone || "-"}</p>
            <p><strong>Email:</strong> {visitorSummary.email || "-"}</p>
            <p><strong>Total Visits:</strong> {visitorSummary.totalVisits}</p>
            <p><strong>Last Visit:</strong> {formatDate(visitorSummary.lastVisitedAt)}</p>
          </div>
        ) : (
          <p className="muted">No existing visitor found. History will appear after first saved visit.</p>
        )}

        <div className="history-list">
          {history.length === 0 ? (
            <p className="muted">No visit records yet.</p>
          ) : (
            history.map((item) => (
              <article key={item.id} className="history-item">
                <p><strong>{formatDate(item.visitedAt)}</strong></p>
                <p>Purpose: {item.purpose || "-"}</p>
                <p>Notes: {item.notes || "-"}</p>
              </article>
            ))
          )}
        </div>
      </aside>
    </section>
  );
}

