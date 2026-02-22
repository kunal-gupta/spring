import React from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import "./styles.css";

const rootElement = document.getElementById("root");

function showBootError(message) {
  if (!rootElement) return;
  rootElement.innerHTML = `<pre style="padding:12px;color:#a6212b;white-space:pre-wrap;">UI boot error:\n${message}</pre>`;
}

window.addEventListener("error", (event) => {
  if (event?.error?.message) {
    showBootError(event.error.message);
  }
});

window.addEventListener("unhandledrejection", (event) => {
  showBootError(String(event.reason || "Unhandled promise rejection"));
});

try {
  createRoot(rootElement).render(
    <React.StrictMode>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </React.StrictMode>
  );
} catch (error) {
  showBootError(error instanceof Error ? error.stack || error.message : String(error));
}
