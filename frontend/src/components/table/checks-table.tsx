import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { ChartLine } from "lucide-react";
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { getChecks } from "@/lib/api/checks";
import { formatElapsedTime, formatNumber } from "@/lib/number";
import { ErrorState } from "../error-state";
import { LoadingState } from "../loading-state";

const AVAILABLE_CHECKS_KEY = "availableChecks";

export function ChecksTable() {
  const navigate = useNavigate();

  const {
    isPending,
    error,
    data: checks,
  } = useQuery({
    queryKey: [AVAILABLE_CHECKS_KEY],
    queryFn: getChecks,
  });
  const [checkboxes, setCheckboxes] = useState<boolean[]>([]);

  useEffect(() => {
    if (checks) {
      setCheckboxes(Array(checks.length).fill(false));
    }
  }, [checks]);

  const handleAllCheckboxesChange = (checked: boolean) => {
    if (!checks) {
      return;
    }

    setCheckboxes(Array(checks.length).fill(checked));
  };

  const handleCheckboxChange = (idx: number) => {
    const newCheckboxes = [...checkboxes];
    newCheckboxes[idx] = !newCheckboxes[idx];
    setCheckboxes(newCheckboxes);
  };

  const handleShowGraph = () => {
    // TODO: show check history graph
    alert("graph is being shown");
  };

  const handleSubmitButtonClick = () => {
    if (!checks) {
      return;
    }

    const selectedChecks = checks
      .filter((_, idx) => checkboxes[idx])
      .map((check) => check.name);

    if (selectedChecks.length === 0) {
      // TODO: give user some feedback
      return;
    }

    setCheckboxes(Array(checks.length).fill(false));

    navigate({
      to: "/results",
      search: {
        checks: selectedChecks,
      },
    });
  };

  if (isPending) {
    return <LoadingState />;
  }

  if (error) {
    return (
      <ErrorState
        message="Failed to load checks!"
        invalidateQueryKey={[AVAILABLE_CHECKS_KEY]}
      />
    );
  }

  return (
    <div className="flex flex-col gap-4 md:gap-6">
      <Table>
        <TableHeader>
          <TableRow className="*:text-center *:font-bold *:capitalize">
            <TableHead className="max-w-6">
              <div className="flex items-center justify-center">
                <Checkbox
                  className="cursor-pointer"
                  onCheckedChange={(checked) =>
                    handleAllCheckboxesChange(!!checked)
                  }
                />
              </div>
            </TableHead>

            <TableHead>Name</TableHead>

            <TableHead className="hidden md:table-cell">Last Result</TableHead>

            <TableHead className="hidden md:table-cell">Last Date</TableHead>

            <TableHead className="hidden xl:table-cell">
              Last Execution
            </TableHead>

            <TableHead className="hidden sm:table-cell">Actions</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {checks.map((check, idx) => (
            <TableRow key={idx} className="*:text-center">
              <TableCell>
                <div className="flex items-center justify-center">
                  <Checkbox
                    className="cursor-pointer"
                    checked={checkboxes[idx] || false}
                    onCheckedChange={() => handleCheckboxChange(idx)}
                  />
                </div>
              </TableCell>

              <TableCell>{check.name}</TableCell>

              <TableCell className="hidden md:table-cell">
                {check.lastResult != null
                  ? formatNumber(check.lastResult)
                  : "-"}
              </TableCell>

              <TableCell className="hidden md:table-cell">
                {check.lastTimestamp != null
                  ? check.lastTimestamp.toLocaleDateString()
                  : "-"}
              </TableCell>

              <TableCell className="hidden xl:table-cell">
                {check.lastExecutionTime != null
                  ? formatElapsedTime(check.lastExecutionTime)
                  : "-"}
              </TableCell>

              <TableCell className="hidden sm:table-cell">
                <Button
                  className="cursor-pointer"
                  variant="ghost"
                  disabled={check.lastResult == null}
                  onClick={handleShowGraph}
                >
                  <ChartLine />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Button
        className="w-full max-w-120 cursor-pointer self-center"
        onClick={handleSubmitButtonClick}
      >
        Execute
      </Button>
    </div>
  );
}
