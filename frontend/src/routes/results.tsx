import { Link, createFileRoute, redirect } from "@tanstack/react-router";
import { ChevronLeft } from "lucide-react";
import { useMemo } from "react";
import { PageLayout } from "@/components/layout/page-layout";
import { ResultsTable } from "@/components/results-table/results-table";
import { Title } from "@/components/title";

type ChecksArray = string[];

interface CheckResultsSearch {
  checks: ChecksArray;
}

function checkResultsValidateSearch(
  search: Record<string, unknown>,
): CheckResultsSearch {
  const checksSearchParam = search.checks;

  if (
    Array.isArray(checksSearchParam) &&
    checksSearchParam.every((item) => typeof item === "string")
  ) {
    return {
      checks: checksSearchParam as ChecksArray,
    };
  }

  if (typeof checksSearchParam === "string") {
    const checks = checksSearchParam
      .toLocaleLowerCase()
      .split(",")
      .map((check) => check.trim());

    return {
      checks,
    };
  }

  return {
    checks: [],
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

  const selectedChecks = useMemo(
    () =>
      checks.map((check) => ({
        name: check,
      })),
    [checks],
  );

  return (
    <PageLayout>
      <Title>
        <Link className="p-1 md:p-2" to="/">
          <ChevronLeft className="size-4 md:size-5 lg:size-6" />
        </Link>
        Check Results
      </Title>

      <ResultsTable checks={selectedChecks} />
    </PageLayout>
  );
}
