import { CartesianGrid, Line, LineChart, XAxis, YAxis } from "recharts";
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";
import { CheckHistory } from "@/types/checks";

interface ResultChartProps {
  className?: string;
  checkHistories: CheckHistory[];
}

const chartConfig = {
  result: {
    label: "Result",
    color: "var(--color-rose-500)",
  },
} satisfies ChartConfig;

export function ResultChart({ className, checkHistories }: ResultChartProps) {
  return (
    <ChartContainer className={className} config={chartConfig}>
      <LineChart
        accessibilityLayer
        data={checkHistories}
        margin={{ left: 12, right: 12 }}
      >
        <CartesianGrid vertical={true} />

        <XAxis
          dataKey="timestamp"
          tickLine={false}
          axisLine={false}
          tickMargin={12}
          tickFormatter={(value: Date) => value.toLocaleDateString()}
        />

        <YAxis
          dataKey="result"
          tickLine={false}
          axisLine={false}
          tickMargin={12}
          domain={[
            (dataMin: number) => Math.floor(dataMin * 0.95),
            (dataMax: number) => Math.ceil(dataMax * 1.05),
          ]}
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
