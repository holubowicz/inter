import { Check, CheckResult } from "@/types/check";

export async function getChecks(): Promise<Check[]> {
  const res = await fetch("http://localhost:8080/api/checks");

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(
      `Failed to fetch checks: ${res.status} ${res.statusText} - ${errorText}`,
    );
  }

  return res.json();
}

export async function runChecks(checks: Check[]): Promise<CheckResult[]> {
  const res = await fetch("http://localhost:8080/api/checks/run", {
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
