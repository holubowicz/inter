export interface CheckDTO {
  name: string;
}

export interface Check {
  result: number;
  executionTime: number;
  timestamp: Date;
}

export interface AvailableCheck {
  name: string;
  lastCheck: Nullable<Check>;
}

export interface CheckResult {
  name: Nullable<string>;
  error: Nullable<string>;
  trendPercentage: Nullable<number>;
  check: Nullable<Check>;
  lastCheck: Nullable<Check>;
}
