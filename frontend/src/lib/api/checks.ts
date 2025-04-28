import { Check, CheckResult } from "@/types/check";

const API_URL = import.meta.env.VITE_API_URL;

export async function getChecks(): Promise<Check[]> {
  const res = await fetch(API_URL + "/api/checks");

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(
      `Failed to fetch checks: ${res.status} ${res.statusText} - ${errorText}`,
    );
  }

  return res.json();
}

export async function runChecks(checks: Check[]): Promise<CheckResult[]> {
  const res = await fetch(API_URL + "/api/checks/run", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(checks),
  });

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(
      `Failed to run checks: ${res.status} ${res.statusText} - ${errorText}`,
    );
  }

  return res.json();
}
