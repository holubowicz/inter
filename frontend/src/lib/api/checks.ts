import { Check, CheckDTO, CheckHistory, CheckResult } from "@/types/checks";

function getApiUrl(path: string): URL {
  const baseUrl = import.meta.env.VITE_API_URL.trim();

  if (!path.trim()) {
    return new URL(baseUrl);
  }

  return new URL(
    path.replace(/^\/+/, ""),
    baseUrl.endsWith("/") ? baseUrl : baseUrl + "/",
  );
}

export async function getChecks(): Promise<Check[]> {
  const res = await fetch(getApiUrl("api/checks"));

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
    lastTimestamp: item.lastTimestamp ? new Date(item.lastTimestamp) : null,
  }));
}

export async function getCheckHistories(
  checkName: string,
): Promise<CheckHistory[]> {
  const res = await fetch(getApiUrl(`api/checks/${checkName}/history`));

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

export async function runChecks(checks: CheckDTO[]): Promise<CheckResult[]> {
  const res = await fetch(getApiUrl("api/checks/run"), {
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
    lastTimestamp: item.lastTimestamp ? new Date(item.lastTimestamp) : null,
  }));
}
