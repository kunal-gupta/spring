import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { apiRequest, setToken } from "../api";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function updateField(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setLoading(true);
    try {
      const result = await apiRequest("/auth/register", {
        method: "POST",
        body: JSON.stringify(form)
      });
      setToken(result.token);
      navigate("/visit", { replace: true });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="panel auth-panel">
      <h2>Create Account</h2>
      <p className="muted">Register once, then use login for next sessions.</p>
      <form onSubmit={handleSubmit} className="form-grid">
        <label>
          Username
          <input name="username" value={form.username} onChange={updateField} required />
        </label>
        <label>
          Password
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={updateField}
            minLength={6}
            required
          />
        </label>
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={loading}>{loading ? "Registering..." : "Register"}</button>
      </form>
    </section>
  );
}

