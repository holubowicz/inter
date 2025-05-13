import { useQuery } from "@tanstack/react-query";
import {
  Table,
  TableBody,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { runChecks } from "@/lib/api/checks";
import { CheckMetadata } from "@/types/checks";
import { ErrorState } from "../error-state";
import { LoadingState } from "../loading-state";
import { ResultsTableRow } from "./results-table-row";

interface ResultsTableProps {
  checks: CheckMetadata[];
}

const CHECK_RESULTS_KEY = "checkResults";
const CHECK_RESULTS_QUERY_STALE_TIME = 30 * 1000;

export function ResultsTable({ checks }: ResultsTableProps) {
  const {
    isPending,
    error,
    data: checkResults,
  } = useQuery({
    queryKey: [CHECK_RESULTS_KEY, checks],
    queryFn: () => runChecks(checks),
    staleTime: CHECK_RESULTS_QUERY_STALE_TIME,
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
      <TableHeader>
        <TableRow className="*:text-center *:font-bold *:capitalize">
          <TableHead>Name</TableHead>
          <TableHead>Result</TableHead>
          <TableHead className="hidden xl:table-cell">Execution Time</TableHead>
          <TableHead className="hidden md:table-cell">Trend</TableHead>
          <TableHead className="hidden lg:table-cell">Last Result</TableHead>
          <TableHead className="hidden xl:table-cell">Last Execution</TableHead>
          <TableHead>Actions</TableHead>
        </TableRow>
      </TableHeader>

      <TableBody>
        {checkResults.map((result, idx) => (
          <ResultsTableRow key={idx} check={checks[idx]} checkResult={result} />
        ))}
      </TableBody>
    </Table>
  );
}
