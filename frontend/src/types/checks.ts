export interface CheckMetadata {
  name: string;
  category: string;
}

export interface Check {
  result: number;
  executionTime: number;
  timestamp: Date;
}

export interface AvailableCheck {
  metadata: CheckMetadata;
  lastCheck: Nullable<Check>;
}

export interface CheckResult {
  error: Nullable<string>;
  trendPercentage: Nullable<number>;
  metadata: CheckMetadata;
  check: Nullable<Check>;
  lastCheck: Nullable<Check>;
}
