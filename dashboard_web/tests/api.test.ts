import { describe, expect, it, vi } from "vitest";
import { dashboardApi } from "@/lib/api";

describe("dashboardApi", () => {
  it("throws when the server returns a non-success response", async () => {
    vi.stubGlobal("fetch", vi.fn(() => Promise.resolve(new Response("", { status: 500 }))) as typeof fetch);

    await expect(dashboardApi.summary()).rejects.toThrow("Request failed: 500");
  });
});
