import {
  AvailableCheck,
  Check,
  CheckMetadata,
  CheckResult,
} from "@/types/checks";

function getApiBase(): string {
  const { host, protocol } = window.location;
  return import.meta.env.VITE_API_URL?.trim() ?? `${protocol}//${host}/api`;
}

function getApiUrl(path: string): string {
  const base = getApiBase();
  return `${base.replace(/\/$/, "")}/${path.replace(/^\/+/, "")}`;
}

export async function getChecks(): Promise<AvailableCheck[]> {
  const res = await fetch(getApiUrl("checks"));

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(
      `Failed to fetch checks: ${res.status} ${res.statusText} - ${errorText}`,
    );
  }

  const data = await res.json();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  return data.map((item: any) => ({
    ...item,
    lastCheck: item.lastCheck
      ? { ...item.lastCheck, timestamp: new Date(item.lastCheck.timestamp) }
      : null,
  }));
}

export async function getCheckHistories(checkName: string): Promise<Check[]> {
  const res = await fetch(getApiUrl(`checks/${checkName}/history`));

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(
      `Failed to fetch check histories: ${res.status} ${res.statusText} - ${errorText}`,
    );
  }

  const data = await res.json();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  return data.map((item: any) => ({
    ...item,
    timestamp: new Date(item.timestamp),
  }));
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
    const errorText = await res.text();
    throw new Error(
      `Failed to run checks: ${res.status} ${res.statusText} - ${errorText}`,
    );
  }

  const data = await res.json();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  return data.map((item: any) => ({
    ...item,
    check: item.check
      ? { ...item.check, timestamp: new Date(item.check.timestamp) }
      : null,
    lastCheck: item.lastCheck
      ? { ...item.lastCheck, timestamp: new Date(item.lastCheck.timestamp) }
      : null,
  }));
}
