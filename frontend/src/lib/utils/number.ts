export function formatNumber(num: number, decimalPoints = 2): number {
  const factor = 10 ** decimalPoints;
  const rounded = Math.round(num * factor) / factor;
  return Number.isInteger(rounded)
    ? rounded
    : parseFloat(rounded.toFixed(decimalPoints));
}

const units = [
  { label: "d", value: 24 * 60 * 60 * 1000 },
  { label: "h", value: 60 * 60 * 1000 },
  { label: "min", value: 60 * 1000 },
  { label: "s", value: 1000 },
  { label: "ms", value: 1 },
];

export function formatElapsedTime(ms: number): string {
  for (const unit of units) {
    if (ms >= unit.value) {
      const value = ms / unit.value;
      return `${parseFloat(value.toFixed(2))}${unit.label}`;
    }
  }
  return "0ms";
}
