import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { ChartLine } from "lucide-react";
import { useCallback, useEffect, useState } from "react";
import { toast } from "sonner";
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
import { ErrorState } from "@/components/error-state";
import { LoadingState } from "@/components/loading-state";
import { getChecks } from "@/lib/api/checks";
import { formatDateTime } from "@/lib/utils/date";
import { formatElapsedTime, formatNumber } from "@/lib/utils/number";

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

  const handleAllCheckboxesChange = useCallback(
    (checked: boolean) => {
      if (!checks) {
        return;
      }

      setCheckboxes(Array(checks.length).fill(checked));
    },
    [checks],
  );

  const handleCheckboxChange = useCallback(
    (idx: number) => {
      const newCheckboxes = [...checkboxes];
      newCheckboxes[idx] = !newCheckboxes[idx];
      setCheckboxes(newCheckboxes);
    },
    [checkboxes],
  );

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

  const handleSubmitButtonClick = useCallback(() => {
    if (!checks) {
      return;
    }

    const selectedChecks = checks
      .filter((_, idx) => checkboxes[idx])
      .map((check) => check.name);

    if (selectedChecks.length === 0) {
      toast.info("No Checks Selected", {
        description: "Please select at least 1 check, then click execute",
      });
      return;
    }

    setCheckboxes(Array(checks.length).fill(false));

    navigate({
      to: "/results",
      search: {
        checks: selectedChecks,
      },
    });
  }, [checks, checkboxes, navigate]);

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

            <TableHead className="hidden md:table-cell">
              Last Execution
            </TableHead>

            <TableHead className="hidden xl:table-cell">
              Last Execution Time
            </TableHead>

            <TableHead>Actions</TableHead>
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
                {check.lastCheck != null
                  ? formatNumber(check.lastCheck.result)
                  : "-"}
              </TableCell>

              <TableCell className="hidden md:table-cell">
                {check.lastCheck != null
                  ? formatDateTime(check.lastCheck.timestamp)
                  : "-"}
              </TableCell>

              <TableCell className="hidden xl:table-cell">
                {check.lastCheck != null
                  ? formatElapsedTime(check.lastCheck.executionTime)
                  : "-"}
              </TableCell>

              <TableCell>
                <Button
                  className="cursor-pointer"
                  variant="ghost"
                  disabled={check.lastCheck == null}
                  onClick={() => handleShowHistoryGraph(check.name)}
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
