import { useQuery } from "@tanstack/react-query";
import {
  Table,
  TableBody,
  TableCaption,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Check, CheckResult } from "@/types/check";
import { ErrorState } from "../error-state";
import { LoadingState } from "../loading-state";
import { ResultsTableRow } from "./results-table-row";

interface ResultsTableProps {
  checks: Check[];
}

const CHECK_RESULTS_KEY = "checkResults";

async function runChecks(checks: Check[]): Promise<CheckResult[]> {
  const res = await fetch("http://localhost:8080/api/checks/run", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(checks),
  });

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(
      `Failed to run checks: ${res.status} ${res.statusText} - ${errorText}`,
    );
  }

  return res.json();
}

export function ResultsTable({ checks }: ResultsTableProps) {
  const {
    isPending,
    error,
    data: checkResults,
  } = useQuery({
    queryKey: [CHECK_RESULTS_KEY, checks],
    queryFn: () => runChecks(checks),
  });

  if (isPending) {
    return <LoadingState />;
  }

  if (error) {
    return (
      <ErrorState
        message="Failed to get check results!"
        invalidateQueryKey={[CHECK_RESULTS_KEY, checks]}
      />
    );
  }

  return (
    <Table>
      <TableCaption>Ran checks results</TableCaption>

      <TableHeader>
        <TableRow className="*:text-center *:font-bold *:capitalize">
          <TableHead>Name</TableHead>

          <TableHead>Result</TableHead>

          <TableHead>Last Result</TableHead>

          <TableHead>Trend</TableHead>

          <TableHead></TableHead>
        </TableRow>
      </TableHeader>

      <TableBody>
        {checkResults.map((checkResult, idx) => (
          <ResultsTableRow key={idx} checkResult={checkResult} />
        ))}
      </TableBody>
    </Table>
  );
}
