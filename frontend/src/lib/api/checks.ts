import {
  AvailableCheck,
  AvailableCheckSchema,
  Check,
  CheckMetadata,
  CheckResult,
  CheckResultSchema,
  CheckSchema,
} from "@/types/checks";
import { getApiUrl } from ".";

export async function getChecks(): Promise<AvailableCheck[]> {
  const res = await fetch(getApiUrl("checks"));

  if (!res.ok) {
    throw new Error(
      `Failed to fetch checks: ${res.status} ${res.statusText} - ${await res.text()}`,
    );
  }

  const data = await res.json();
  return AvailableCheckSchema.array().parse(data);
}

export async function getCheckHistories(checkName: string): Promise<Check[]> {
  const res = await fetch(getApiUrl(`checks/${checkName}/history`));

  if (!res.ok) {
    throw new Error(
      `Failed to fetch check histories: ${res.status} ${res.statusText} - ${await res.text()}`,
    );
  }

  const data = await res.json();
  return CheckSchema.array().parse(data);
}

export async function runChecks(
  checks: CheckMetadata[],
): Promise<CheckResult[]> {
  const res = await fetch(getApiUrl("checks/run"), {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(checks),
  });

  if (!res.ok) {
    throw new Error(
      `Failed to run checks: ${res.status} ${res.statusText} - ${await res.text()}`,
    );
  }

  const data = await res.json();
  return CheckResultSchema.array().parse(data);
}
