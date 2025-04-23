export interface Check {
  name: string;
}

export interface CheckResult {
  name: string;
  error: string;
  result: number;
  lastResult: number;
  trendPercentage: number;
}
