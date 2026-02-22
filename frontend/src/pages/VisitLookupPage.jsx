import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { apiRequest } from "../api";

export default function VisitLookupPage() {
  const navigate = useNavigate();
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");

    if (!phone && !email) {
      setError("Enter mobile number or email.");
      return;
    }

    setLoading(true);

    let visitorSummary = null;
    try {
      const query = new URLSearchParams();
      if (phone) query.set("phone", phone);
      if (email) query.set("email", email);
      visitorSummary = await apiRequest(`/api/visitors/search?${query.toString()}`);
    } catch (err) {
      // Expected when visitor does not exist; user can still log a new visitor.
      visitorSummary = null;
    } finally {
      setLoading(false);
    }

    navigate("/visit/workspace", {
      state: {
        lookupPhone: phone,
        lookupEmail: email,
        visitorSummary
      }
    });
  }

  return (
    <section className="panel lookup-panel">
      <h2>Visit Lookup</h2>
      <p className="muted">Enter mobile and/or email, then continue to visit logging screen.</p>
      <form onSubmit={handleSubmit} className="form-grid two-col">
        <label>
          Mobile Number
          <input value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="9876543210" />
        </label>
        <label>
          Email
          <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="visitor@example.com" />
        </label>
        {error && <p className="error full-width">{error}</p>}
        <button className="full-width" type="submit" disabled={loading}>
          {loading ? "Checking..." : "Continue"}
        </button>
      </form>
    </section>
  );
}

