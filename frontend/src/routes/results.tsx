import { createFileRoute, redirect } from "@tanstack/react-router";
import { GoBackTitle } from "@/components/go-back-title";
import { PageLayout } from "@/components/layout/page-layout";
import { ResultsTable } from "@/components/results-table/results-table";
import { CheckMetadata } from "@/types/checks";

interface CheckResultsSearch {
  checks: CheckMetadata[];
}

function checkResultsValidateSearch(
  search: Record<string, unknown>,
): CheckResultsSearch {
  const checksSearchParam = search.checks;

  return {
    checks: checksSearchParam as CheckMetadata[],
  };
}

function checkResultsBeforeLoad({ search }: { search: CheckResultsSearch }) {
  const { checks } = search;

  if (checks.length === 0) {
    throw redirect({ to: "/" });
  }
}

export const Route = createFileRoute("/results")({
  validateSearch: checkResultsValidateSearch,
  beforeLoad: checkResultsBeforeLoad,
  component: CheckResultsPage,
});

function CheckResultsPage() {
  const { checks } = Route.useSearch();

  return (
    <PageLayout>
      <GoBackTitle>Check Results</GoBackTitle>

      <ResultsTable checks={checks} />
    </PageLayout>
  );
}
