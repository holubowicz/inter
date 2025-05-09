import { useMemo } from "react";
import { CartesianGrid, Line, LineChart, XAxis, YAxis } from "recharts";
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";
import { Check } from "@/types/checks";

interface ResultChartProps {
  className?: string;
  checks: Check[];
}

const chartConfig = {
  result: {
    label: "Result",
    color: "var(--color-rose-500)",
  },
} satisfies ChartConfig;

export function ResultChart({ className, checks }: ResultChartProps) {
  const chartData = useMemo(
    () =>
      checks.map((check) => ({
        dateTime: check.timestamp.toLocaleString(),
        result: check.result,
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

        <XAxis
          dataKey="dateTime"
          tickLine={false}
          axisLine={false}
          tickMargin={12}
          minTickGap={12}
        />

        <YAxis
          dataKey="result"
          tickLine={false}
          axisLine={false}
          tickMargin={6}
          domain={[
            (min: number) => Math.floor(min * 0.95),
            (max: number) => Math.ceil(max * 1.05),
          ]}
        />

        <ChartTooltip cursor={false} content={<ChartTooltipContent />} />

        <Line
          dataKey="result"
          type="natural"
          stroke="var(--color-result)"
          strokeWidth={2}
          dot={false}
        />
      </LineChart>
    </ChartContainer>
  );
}
