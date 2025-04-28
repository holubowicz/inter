import { Check, CheckDTO, CheckResult } from "@/types/checks";

function getApiUrl(path: string) {
  return new URL(`${import.meta.env.VITE_API_URL}/${path}`);
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
