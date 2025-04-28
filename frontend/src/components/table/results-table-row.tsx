import { useMutation } from "@tanstack/react-query";
import {
  ChartLine,
  MoveRight,
  RotateCcw,
  TrendingDown,
  TrendingUp,
} from "lucide-react";
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { TableCell, TableRow } from "@/components/ui/table";
import { runChecks } from "@/lib/api/checks";
import { formatNumber } from "@/lib/number";
import { Check, CheckResult } from "@/types/check";

interface ResultsTableRowProps {
  check: Check;
  checkResult: CheckResult;
}

function getResultState(checkResult: CheckResult) {
  if (checkResult.error != null || checkResult.result == null) {
    return {
      result: "-",
      lastResult: "-",
      trendPercentage: "-",
      isDisabled: true,
      trendIcon: <MoveRight className="text-muted-foreground w-4" />,
    };
  }

  let trendIcon = <MoveRight className="text-muted-foreground w-4" />;
  let lastResult = "-";
  let trendPercentage = "-";

  if (checkResult.lastResult != null) {
    lastResult = formatNumber(checkResult.lastResult).toString();
  }

  if (checkResult.trendPercentage != null) {
    trendPercentage =
      formatNumber(checkResult.trendPercentage).toString() + "%";

    if (checkResult.trendPercentage > 0) {
      trendIcon = <TrendingUp className="w-4 text-green-600" />;
    } else if (checkResult.trendPercentage < 0) {
      trendIcon = <TrendingDown className="w-4 text-red-600" />;
    }
  }

  return {
    result: formatNumber(checkResult.result).toString(),
    lastResult,
    trendPercentage,
    isDisabled: false,
    trendIcon,
  };
}

export function ResultsTableRow({ check, checkResult }: ResultsTableRowProps) {
  const [resultState, setResultState] = useState(() =>
    getResultState(checkResult),
  );

  useEffect(() => {
    setResultState(getResultState(checkResult));
  }, [checkResult]);

  const refetchMutation = useMutation({
    mutationFn: async () => {
      const results = await runChecks([check]);
      return results[0];
    },
    onSuccess: (result) => {
      setResultState(getResultState(result));
    },
    onError: () => {
      setResultState(
        getResultState({ ...checkResult, error: "Failed to refetch check" }),
      );
    },
  });

  const handleRefetch = () => {
    refetchMutation.mutate();
  };

  const handleShowGraph = () => {
    // TODO: show check history graph
    alert("graph is being shown");
  };

  return (
    <TableRow className="*:text-center">
      <TableCell>{checkResult.name}</TableCell>

      <TableCell>{resultState.result}</TableCell>

      <TableCell className="hidden xl:table-cell"></TableCell>

      <TableCell className="hidden md:table-cell">
        <div className="flex items-center justify-center gap-2">
          <span>{resultState.trendPercentage}</span>
          {resultState.trendPercentage != "-" && resultState.trendIcon}
        </div>
      </TableCell>

      <TableCell className="hidden lg:table-cell">
        {resultState.lastResult}
      </TableCell>

      <TableCell className="hidden xl:table-cell"></TableCell>

      <TableCell className="hidden sm:table-cell">
        <Button
          className="cursor-pointer"
          variant="ghost"
          disabled={resultState.isDisabled || refetchMutation.isPending}
          onClick={handleRefetch}
        >
          {refetchMutation.isPending ? (
            <RotateCcw className="direction-reverse animate-spin" />
          ) : (
            <RotateCcw />
          )}
        </Button>

        <Button
          className="cursor-pointer"
          variant="ghost"
          disabled={resultState.isDisabled || refetchMutation.isPending}
          onClick={handleShowGraph}
        >
          <ChartLine />
        </Button>
      </TableCell>
    </TableRow>
  );
}
