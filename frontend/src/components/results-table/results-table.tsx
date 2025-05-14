import { useQuery } from "@tanstack/react-query";
import {
  CompactTableHead,
  Table,
  TableBody,
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
          <CompactTableHead className="text-left!">Name</CompactTableHead>

          <CompactTableHead className="hidden text-left! sm:table-cell">
            Category
          </CompactTableHead>

          <CompactTableHead className="text-right!">Result</CompactTableHead>

          <CompactTableHead className="hidden text-right! xl:table-cell">
            Execution Time
          </CompactTableHead>

          <CompactTableHead className="hidden lg:table-cell">
            Trend
          </CompactTableHead>

          <CompactTableHead className="hidden text-right! md:table-cell">
            Last Result
          </CompactTableHead>

          <CompactTableHead className="hidden xl:table-cell">
            Last Execution
          </CompactTableHead>

          <CompactTableHead>Actions</CompactTableHead>
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
