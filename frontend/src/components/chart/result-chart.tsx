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

// TODO: fix the XAxis DateTime to be more readable, potentially migrate to Chart.js
// TODO: add to the ChartTooltip also corresponding timestamp

export function ResultChart({ className, checks }: ResultChartProps) {
  return (
    <ChartContainer className={className} config={chartConfig}>
      <LineChart
        accessibilityLayer
        data={checks}
        margin={{ left: 12, right: 12 }}
      >
        <CartesianGrid vertical={true} />

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

        <XAxis
          dataKey="timestamp"
          tickLine={false}
          axisLine={false}
          tickMargin={12}
          minTickGap={12}
          tickFormatter={(value: Date) => value.toLocaleString()}
        />

        <ChartTooltip
          cursor={false}
          content={<ChartTooltipContent hideLabel />}
        />

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
