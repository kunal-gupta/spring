import { describe, expect, it } from "vitest";
import { calculateGrossMargin } from "@/lib/metrics";

describe("calculateGrossMargin", () => {
  it("calculates margin to one decimal place", () => {
    expect(calculateGrossMargin(100, 35)).toBe(65);
  });

  it("returns zero when revenue is zero", () => {
    expect(calculateGrossMargin(0, 50)).toBe(0);
  });
});
