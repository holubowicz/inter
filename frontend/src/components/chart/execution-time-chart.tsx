import { useMemo } from "react";
import { CartesianGrid, Line, LineChart, XAxis, YAxis } from "recharts";
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";
import { formatDateTime } from "@/lib/utils/date";
import { Check } from "@/types/checks";

interface ExecutionTimeChartProps {
  className?: string;
  checks: Check[];
}

const chartConfig = {
  executionTime: {
    label: "Execution [ms]",
    color: "var(--color-emerald-500)",
  },
} satisfies ChartConfig;

export function ExecutionTimeChart({
  className,
  checks,
}: ExecutionTimeChartProps) {
  const chartData = useMemo(
    () =>
      checks.map((check) => ({
        dateTime: formatDateTime(check.timestamp),
        executionTime: check.executionTime,
      })),
    [checks],
  );

  return (
    <ChartContainer className={className} config={chartConfig}>
      <LineChart
        accessibilityLayer
        data={chartData}
        margin={{ left: 12, right: 12 }}
      >
        <CartesianGrid vertical={false} />

        <YAxis
          dataKey="executionTime"
          unit="ms"
          tickLine={false}
          axisLine={false}
          tickMargin={12}
          domain={[
            (min: number) => Math.floor(min * 0.95),
            (max: number) => Math.ceil(max * 1.05),
          ]}
        />

        <XAxis
          dataKey="dateTime"
          tickLine={false}
          axisLine={false}
          tickMargin={12}
          minTickGap={12}
        />

        <ChartTooltip cursor={false} content={<ChartTooltipContent />} />

        <Line
          dataKey="executionTime"
          type="natural"
          stroke="var(--color-executionTime)"
          strokeWidth={2}
          dot={false}
        />
      </LineChart>
    </ChartContainer>
  );
}
