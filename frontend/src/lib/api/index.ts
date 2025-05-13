function getApiBase(): string {
  const { host, protocol } = window.location;
  return import.meta.env.VITE_API_URL?.trim() ?? `${protocol}//${host}/api`;
}

export function getApiUrl(path: string): string {
  const base = getApiBase();
  return `${base.replace(/\/$/, "")}/${path.replace(/^\/+/, "")}`;
}
