import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { getChecks } from "@/lib/api/checks";
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

  const handleSubmit = () => {
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
        <TableCaption>All available checks</TableCaption>

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
          </TableRow>
        </TableHeader>

        <TableBody>
          {checks.map(({ name }, idx) => (
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

              <TableCell>{name}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Button
        className="w-full max-w-120 cursor-pointer self-center"
        onClick={handleSubmit}
      >
        Execute
      </Button>
    </div>
  );
}
