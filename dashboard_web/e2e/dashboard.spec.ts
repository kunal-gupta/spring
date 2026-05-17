import { expect, test } from "@playwright/test";

test("dashboard landing page tells the opening story", async ({ page }) => {
  await page.goto("/");

  await expect(page.getByRole("heading", { name: /a dashboard should read like a business story/i })).toBeVisible();
  await expect(page.getByText("Revenue", { exact: true }).first()).toBeVisible();
  await expect(page.getByText("Gross margin")).toBeVisible();
  await expect(page.getByText(/reporting period:/i)).toBeVisible();
  await expect(page.getByRole("tab", { name: /profitability/i })).toBeVisible();

  await page.getByRole("tab", { name: /refunds & quality/i }).click();
  await expect(page.getByRole("heading", { name: /refund pressure/i })).toBeVisible();

  await page.getByRole("button", { name: /undock sidebar/i }).click();
  await expect(page.getByRole("button", { name: /dock sidebar/i })).toBeVisible();
  await page.getByRole("button", { name: /dock sidebar/i }).click();

  await page.getByRole("tab", { name: /sales insight/i }).click();
  await expect(page.getByText(/business scorecard/i)).toBeVisible();
  await expect(page.getByText(/kpi tiles/i)).toBeVisible();
  await expect(page.getByText(/grid summary/i)).toBeVisible();

  await page.getByRole("tab", { name: /storyboard/i }).click();
  await expect(page.getByText(/more promising layout/i)).toBeVisible();
});
