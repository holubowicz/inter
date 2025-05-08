import { CartesianGrid, Line, LineChart, XAxis, YAxis } from "recharts";
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";
import { Check } from "@/types/checks";

interface ExecutionTimeChartProps {
  className?: string;
  checks: Check[];
}

const chartConfig = {
  executionTime: {
    label: "Execution Time",
    color: "var(--color-emerald-500)",
  },
} satisfies ChartConfig;

export function ExecutionTimeChart({
  className,
  checks,
}: ExecutionTimeChartProps) {
  return (
    <ChartContainer className={className} config={chartConfig}>
      <LineChart
        accessibilityLayer
        data={checks}
        margin={{ left: 12, right: 12 }}
      >
        <CartesianGrid vertical={true} />

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
          dataKey="timestamp"
          tickLine={false}
          axisLine={false}
          tickMargin={12}
          tickFormatter={(value: Date) => value.toLocaleString()}
        />

        <ChartTooltip
          cursor={false}
          content={<ChartTooltipContent hideLabel />}
        />

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
