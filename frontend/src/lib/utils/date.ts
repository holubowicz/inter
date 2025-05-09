import { intlFormat } from "date-fns";

export function formatDateTime(date: Date) {
  return intlFormat(date, {
    year: "numeric",
    month: "numeric",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}
