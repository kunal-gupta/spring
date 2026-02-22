import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { clearToken, hasToken } from "../api";

export default function AppShell({ children }) {
  const navigate = useNavigate();
  const loggedIn = hasToken();

  function handleLogout() {
    clearToken();
    navigate("/login", { replace: true });
  }

  return (
    <div className="app-shell">
      <header className="topbar">
        <h1>Visitor Management</h1>
        <nav>
          <NavLink to="/register">Register</NavLink>
          <NavLink to="/login">Login</NavLink>
          <NavLink to="/visit">Visit</NavLink>
          {loggedIn && (
            <button className="link-button" onClick={handleLogout} type="button">
              Logout
            </button>
          )}
        </nav>
      </header>
      <main>{children}</main>
    </div>
  );
}

