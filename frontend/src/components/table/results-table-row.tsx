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
import { formatNumber } from "@/lib/number";
import { CheckResult } from "@/types/check";

interface ResultsTableRowProps {
  checkResult: CheckResult;
}

export function ResultsTableRow({ checkResult }: ResultsTableRowProps) {
  const [result, setResult] = useState("-");
  const [lastResult, setLastResult] = useState("-");
  const [trendPercentage, setTrendPercentage] = useState("-");
  const [isDisabled, setIsDisabled] = useState(true);
  const [trendIcon, setTrendIcon] = useState(
    <MoveRight className="text-muted-foreground w-4" />,
  );

  useEffect(() => {
    if (checkResult.error != null) {
      return;
    }

    setResult(formatNumber(checkResult.result).toString());
    setIsDisabled(false);

    if (checkResult.lastResult == null) {
      return;
    }

    setLastResult(formatNumber(checkResult.lastResult).toString());
    setTrendPercentage(
      formatNumber(checkResult.trendPercentage).toString() + "%",
    );

    if (checkResult.trendPercentage > 0) {
      setTrendIcon(<TrendingUp className="w-4 text-green-600" />);
    } else if (checkResult.trendPercentage < 0) {
      setTrendIcon(<TrendingDown className="w-4 text-red-600" />);
    } else {
      <MoveRight className="text-muted-foreground w-4" />;
    }
  }, [checkResult]);

  const handleRefetch = () => {
    alert("result is being refetch");
  };

  const handleShowGraph = () => {
    alert("graph is being shown");
  };

  return (
    <TableRow className="*:text-center">
      <TableCell>{checkResult.name}</TableCell>

      <TableCell>{result}</TableCell>

      <TableCell>{lastResult}</TableCell>

      <TableCell>
        <div className="flex items-center justify-center gap-2">
          <span>{trendPercentage}</span>

          {checkResult.lastResult != null && trendIcon}
        </div>
      </TableCell>

      <TableCell>
        <Button
          className="cursor-pointer"
          variant="ghost"
          disabled={isDisabled}
          onClick={handleRefetch}
        >
          <RotateCcw />
        </Button>

        <Button
          className="cursor-pointer"
          variant="ghost"
          disabled={isDisabled}
          onClick={handleShowGraph}
        >
          <ChartLine />
        </Button>
      </TableCell>
    </TableRow>
  );
}
