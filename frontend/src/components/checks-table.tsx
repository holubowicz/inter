import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { ChartLine } from "lucide-react";
import { useCallback, useEffect, useState } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import {
  CompactTableCell,
  CompactTableHead,
  Table,
  TableBody,
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
      .map((check) => ({
        name: check.metadata.name,
        category: check.metadata.category,
      }));

    if (selectedChecks.length === 0) {
      toast("No Checks Selected", {
        description: "Please select at least 1 check, then click execute.",
      });
      return;
    }

    // TODO: fix the main checkbox is not unchecked
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
            <CompactTableHead className="max-w-6">
              <div className="flex items-center justify-center">
                <Checkbox
                  className="cursor-pointer"
                  onCheckedChange={(checked) =>
                    handleAllCheckboxesChange(!!checked)
                  }
                />
              </div>
            </CompactTableHead>

            <CompactTableHead>Name</CompactTableHead>

            <CompactTableHead className="hidden md:table-cell">
              Last Result
            </CompactTableHead>

            <CompactTableHead className="hidden md:table-cell">
              Last Execution
            </CompactTableHead>

            <CompactTableHead className="hidden xl:table-cell">
              Last Execution Time
            </CompactTableHead>

            <CompactTableHead>Actions</CompactTableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {checks.map((check, idx) => (
            <TableRow key={idx} className="*:text-center">
              <CompactTableCell>
                <div className="flex items-center justify-center">
                  <Checkbox
                    className="cursor-pointer"
                    checked={checkboxes[idx] || false}
                    onCheckedChange={() => handleCheckboxChange(idx)}
                  />
                </div>
              </CompactTableCell>

              <CompactTableCell>{check.metadata.name}</CompactTableCell>

              <CompactTableCell className="hidden md:table-cell">
                {check.lastCheck != null
                  ? formatNumber(check.lastCheck.result)
                  : "-"}
              </CompactTableCell>

              <CompactTableCell className="hidden md:table-cell">
                {check.lastCheck != null
                  ? formatDateTime(check.lastCheck.timestamp)
                  : "-"}
              </CompactTableCell>

              <CompactTableCell className="hidden xl:table-cell">
                {check.lastCheck != null
                  ? formatElapsedTime(check.lastCheck.executionTime)
                  : "-"}
              </CompactTableCell>

              <CompactTableCell>
                <Button
                  className="cursor-pointer"
                  variant="ghost"
                  disabled={check.lastCheck == null}
                  onClick={() => handleShowHistoryGraph(check.metadata.name)}
                >
                  <ChartLine />
                </Button>
              </CompactTableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Button
        className="w-full max-w-120 cursor-pointer self-center"
        onClick={handleSubmitButtonClick}
      >
        Execute Checks
      </Button>
    </div>
  );
}
