export function formatNumber(num: number, decimalPoints = 2): number {
  const factor = 10 ** decimalPoints;
  const rounded = Math.round(num * factor) / factor;
  return Number.isInteger(rounded)
    ? rounded
    : parseFloat(rounded.toFixed(decimalPoints));
}
