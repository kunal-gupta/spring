import React from "react";
import { Route, Routes, Navigate } from "react-router-dom";
import AppShell from "./components/AppShell";
import ProtectedRoute from "./components/ProtectedRoute";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import VisitLookupPage from "./pages/VisitLookupPage";
import VisitWorkspacePage from "./pages/VisitWorkspacePage";

export default function App() {
  return (
    <AppShell>
      <Routes>
        <Route path="/" element={<Navigate to="/visit" replace />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/visit"
          element={
            <ProtectedRoute>
              <VisitLookupPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/visit/workspace"
          element={
            <ProtectedRoute>
              <VisitWorkspacePage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </AppShell>
  );
}

