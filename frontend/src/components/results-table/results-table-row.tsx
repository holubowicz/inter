import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import {
  ChartLine,
  MoveRight,
  RotateCcw,
  TrendingDown,
  TrendingUp,
} from "lucide-react";
import { JSX, useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { TableCell, TableRow } from "@/components/ui/table";
import { runChecks } from "@/lib/api/checks";
import { formatElapsedTime, formatNumber } from "@/lib/number";
import { CheckDTO, CheckResult } from "@/types/checks";

interface ResultsTableRowProps {
  check: CheckDTO;
  checkResult: CheckResult;
}

interface ResultTableRowState {
  result: string;
  executionTime: string;
  lastResult: string;
  lastDate: string;
  trendPercentage: string;
  isDisabled: boolean;
  trendIcon: JSX.Element;
}

function buildResultState(result: CheckResult): ResultTableRowState {
  if (result.error || result.result == null) {
    return {
      result: "-",
      executionTime: "-",
      lastResult: "-",
      lastDate: "-",
      trendPercentage: "-",
      isDisabled: true,
      trendIcon: <MoveRight className="text-muted-foreground w-4" />,
    };
  }

  const executionTime = result.executionTime
    ? formatElapsedTime(result.executionTime)
    : "-";

  const lastResult =
    result.lastResult != null
      ? formatNumber(result.lastResult).toString()
      : "-";

  const lastDate =
    result.lastTimestamp != null
      ? result.lastTimestamp.toLocaleDateString()
      : "-";

  let trendPercentage = "-";
  let trendIcon = <MoveRight className="text-muted-foreground w-4" />;

  if (result.trendPercentage != null) {
    trendPercentage = `${formatNumber(result.trendPercentage)}%`;

    if (result.trendPercentage > 0) {
      trendIcon = <TrendingUp className="w-4 text-green-600" />;
    } else if (result.trendPercentage < 0) {
      trendIcon = <TrendingDown className="w-4 text-red-600" />;
    }
  }

  return {
    result: formatNumber(result.result).toString(),
    executionTime,
    lastResult,
    lastDate,
    trendPercentage,
    isDisabled: false,
    trendIcon,
  };
}

export function ResultsTableRow({ check, checkResult }: ResultsTableRowProps) {
  const navigate = useNavigate();

  const [state, setState] = useState(() => buildResultState(checkResult));

  useEffect(() => {
    setState(buildResultState(checkResult));
  }, [checkResult]);

  const refetch = useMutation({
    mutationFn: async () => (await runChecks([check]))[0],
    onSuccess: (result) => setState(buildResultState(result)),
    onError: () =>
      setState(
        buildResultState({
          ...checkResult,
          error: "Failed to refetch check",
        }),
      ),
  });

  const handleRefetch = () => refetch.mutate();

  const handleShowHistoryGraph = (checkName: string) => {
    navigate({
      to: "/checks/$checkName/history",
      params: {
        checkName,
      },
    });
  };

  return (
    <TableRow className="*:text-center">
      <TableCell>{checkResult.name}</TableCell>

      <TableCell>{state.result}</TableCell>

      <TableCell className="hidden xl:table-cell">
        {state.executionTime}
      </TableCell>

      <TableCell className="hidden md:table-cell">
        <div className="flex items-center justify-center gap-2">
          <span>{state.trendPercentage}</span>
          {state.trendPercentage !== "-" && state.trendIcon}
        </div>
      </TableCell>

      <TableCell className="hidden lg:table-cell">{state.lastResult}</TableCell>

      <TableCell className="hidden xl:table-cell">{state.lastDate}</TableCell>

      <TableCell>
        <Button
          className="cursor-pointer"
          variant="ghost"
          disabled={state.isDisabled || refetch.isPending}
          onClick={handleRefetch}
        >
          {refetch.isPending ? (
            <RotateCcw className="direction-reverse animate-spin" />
          ) : (
            <RotateCcw />
          )}
        </Button>

        <Button
          className="cursor-pointer"
          variant="ghost"
          disabled={state.isDisabled || refetch.isPending}
          onClick={() => handleShowHistoryGraph(check.name)}
        >
          <ChartLine />
        </Button>
      </TableCell>
    </TableRow>
  );
}
