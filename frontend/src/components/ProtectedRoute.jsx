import React from "react";
import { Navigate } from "react-router-dom";
import { hasToken } from "../api";

export default function ProtectedRoute({ children }) {
  return hasToken() ? children : <Navigate to="/login" replace />;
}

