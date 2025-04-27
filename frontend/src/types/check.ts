export interface Check {
  name: string;
}

export interface CheckResult {
  name: string;
  error: Nullable<string>;
  result: Nullable<number>;
  lastResult: Nullable<number>;
  trendPercentage: Nullable<number>;
}
