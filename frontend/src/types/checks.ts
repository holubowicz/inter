import { z } from "zod";

// Check Metadata

export const CheckMetadataSchema = z.object({
  name: z.string(),
  category: z.string(),
});

export type CheckMetadata = z.infer<typeof CheckMetadataSchema>;

// Check

export const CheckSchema = z.object({
  result: z.number(),
  executionTime: z.number(),
  timestamp: z.coerce.date(),
});

export type Check = z.infer<typeof CheckSchema>;

// Available Check

export const AvailableCheckSchema = z.object({
  metadata: CheckMetadataSchema,
  lastCheck: CheckSchema.nullable(),
});

export type AvailableCheck = z.infer<typeof AvailableCheckSchema>;

// Check Result

export const CheckResultSchema = z.object({
  error: z.string().nullable(),
  trendPercentage: z.number().nullable(),
  metadata: CheckMetadataSchema,
  check: CheckSchema.nullable(),
  lastCheck: CheckSchema.nullable(),
});

export type CheckResult = z.infer<typeof CheckResultSchema>;
