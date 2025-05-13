import { createFileRoute, redirect } from "@tanstack/react-router";
import { PageLayout } from "@/components/layout/page-layout";
import { ResultsTable } from "@/components/results-table/results-table";
import { GoBackTitle } from "@/components/typography/go-back-title";
import { CheckMetadata, CheckMetadataSchema } from "@/types/checks";

interface CheckResultsSearch {
  checks: CheckMetadata[];
}

function checkResultsValidateSearch(
  search: Record<string, unknown>,
): CheckResultsSearch {
  const checksMetadataSearchParam = search.checks;

  const safeParsedData = CheckMetadataSchema.array().safeParse(
    checksMetadataSearchParam,
  );
  if (!safeParsedData.success) {
    return {
      checks: [],
    };
  }

  return {
    checks: safeParsedData.data,
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
