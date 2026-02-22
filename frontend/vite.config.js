import { defineConfig } from "vite";

export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      "/auth": "http://localhost:8080",
      "/api": "http://localhost:8080"
    }
  }
});
