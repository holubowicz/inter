import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import {
  ChartLine,
  CircleX,
  MoveRight,
  RotateCcw,
  TrendingDown,
  TrendingUp,
} from "lucide-react";
import { JSX, useCallback, useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { CompactTableCell, TableRow } from "@/components/ui/table";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { runChecks } from "@/lib/api/checks";
import { formatDateTime } from "@/lib/utils/date";
import { formatElapsedTime, formatNumber } from "@/lib/utils/number";
import { CheckMetadata, CheckResult } from "@/types/checks";

interface ResultsTableRowProps {
  check: CheckMetadata;
  checkResult: CheckResult;
}

interface RowState {
  name: string;
  category: string;
  error: string;
  trendPercentage: string;
  isDisabled: boolean;
  trendIcon: JSX.Element;
  check: RowStateCheck;
  lastCheck: RowStateCheck;
}

interface RowStateCheck {
  result: string;
  executionTime: string;
  timestamp: string;
}

function buildResultState(checkResult: CheckResult): RowState {
  if (checkResult.error) {
    return {
      name: checkResult.metadata !== null ? checkResult.metadata.name : "-",
      category:
        checkResult.metadata !== null ? checkResult.metadata.category : "-",
      error: checkResult.error,
      trendPercentage: "-",
      isDisabled: true,
      trendIcon: <MoveRight className="text-muted-foreground w-4" />,
      check: {
        result: "-",
        executionTime: "-",
        timestamp: "-",
      },
      lastCheck: {
        result: "-",
        executionTime: "-",
        timestamp: "-",
      },
    };
  }

  let result = "-";
  let executionTime = "-";
  let timestamp = "-";

  if (checkResult.check) {
    result = formatNumber(checkResult.check.result);
    executionTime = formatElapsedTime(checkResult.check.executionTime);
    timestamp = formatDateTime(checkResult.check.timestamp);
  }

  let lastResult = "-";
  let lastExecutionTime = "-";
  let lastTimestamp = "-";

  if (checkResult.lastCheck) {
    lastResult = formatNumber(checkResult.lastCheck.result);
    lastExecutionTime = formatElapsedTime(checkResult.lastCheck.executionTime);
    lastTimestamp = formatDateTime(checkResult.lastCheck.timestamp);
  }

  let trendPercentage = "-";
  let trendIcon = <MoveRight className="text-muted-foreground w-4" />;

  if (checkResult.trendPercentage !== null) {
    trendPercentage = formatNumber(checkResult.trendPercentage) + "%";

    if (checkResult.trendPercentage > 0) {
      trendIcon = <TrendingUp className="w-4 text-green-600" />;
    } else if (checkResult.trendPercentage < 0) {
      trendIcon = <TrendingDown className="w-4 text-red-600" />;
    }
  }

  return {
    name: checkResult.metadata.name !== null ? checkResult.metadata.name : "-",
    category:
      checkResult.metadata !== null ? checkResult.metadata.category : "-",
    error: "-",
    trendPercentage,
    isDisabled: false,
    trendIcon,
    check: {
      result,
      executionTime,
      timestamp,
    },
    lastCheck: {
      result: lastResult,
      executionTime: lastExecutionTime,
      timestamp: lastTimestamp,
    },
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

  const handleRefetch = useCallback(() => {
    refetch.mutate();
  }, [check]);

  const handleShowHistoryGraph = useCallback(
    (checkName: string) => {
      navigate({
        to: "/checks/$checkName/history",
        params: {
          checkName,
        },
      });
    },
    [navigate],
  );

  return (
    <TableRow>
      <CompactTableCell>
        <div className="flex items-center gap-2">
          <span>{state.name}</span>
          {state.error !== "-" && (
            <Tooltip>
              <TooltipTrigger>
                <CircleX className="text-primary size-4" />
              </TooltipTrigger>
              <TooltipContent>
                <p>{state.error}</p>
              </TooltipContent>
            </Tooltip>
          )}
        </div>
      </CompactTableCell>

      <CompactTableCell className="hidden sm:table-cell">
        {state.category}
      </CompactTableCell>

      <CompactTableCell className="text-right">
        {state.check.result}
      </CompactTableCell>

      <CompactTableCell className="hidden text-right xl:table-cell">
        {state.check.executionTime}
      </CompactTableCell>

      <CompactTableCell className="hidden lg:table-cell">
        <div className="flex items-center justify-end gap-2">
          <span>{state.trendPercentage}</span>
          {state.trendPercentage !== "-" && state.trendIcon}
        </div>
      </CompactTableCell>

      <CompactTableCell className="hidden text-right md:table-cell">
        {state.lastCheck.result}
      </CompactTableCell>

      <CompactTableCell className="hidden text-center xl:table-cell">
        {state.lastCheck.timestamp}
      </CompactTableCell>

      <CompactTableCell className="text-center">
        <Button
          className="cursor-pointer"
          variant="ghost"
          size="xs"
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
          size="xs"
          disabled={state.isDisabled || refetch.isPending}
          onClick={() => handleShowHistoryGraph(check.name)}
        >
          <ChartLine />
        </Button>
      </CompactTableCell>
    </TableRow>
  );
}
