export interface Check {
  name: string;
  lastResult: Nullable<number>;
  lastTimestamp: Nullable<Date>;
}

export interface CheckDTO {
  name: string;
}

export interface CheckResult {
  name: string;
  error: Nullable<string>;
  result: Nullable<number>;
  lastResult: Nullable<number>;
  lastTimestamp: Nullable<Date>;
  trendPercentage: Nullable<number>;
}
