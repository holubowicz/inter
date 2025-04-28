export interface CheckDTO {
  name: string;
}

export interface Check {
  name: string;
  lastResult: Nullable<number>;
  lastTimestamp: Nullable<Date>;
  lastExecutionTime: Nullable<number>;
}

export interface CheckResult {
  name: string;
  error: Nullable<string>;
  result: Nullable<number>;
  executionTime: Nullable<number>;
  lastResult: Nullable<number>;
  lastTimestamp: Nullable<Date>;
  trendPercentage: Nullable<number>;
}
