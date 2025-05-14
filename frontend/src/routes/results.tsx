import { createFileRoute, redirect } from "@tanstack/react-router";
import { PageLayout } from "@/components/layout/page-layout";
import { ResultsTable } from "@/components/results-table/results-table";
import { GoBackTitle } from "@/components/typography/go-back-title";
import { Category, CategorySchema } from "@/types/categories";
import { CheckMetadata, CheckMetadataSchema } from "@/types/checks";

interface ResultsPageSearchChecks {
  checks: CheckMetadata[];
  categories?: never;
}

interface ResultsPageSearchCategories {
  checks?: never;
  categories: Category[];
}

type ResultsPageSearch = ResultsPageSearchChecks | ResultsPageSearchCategories;

function resultsPageValidateSearch(
  search: Record<string, unknown>,
): ResultsPageSearch {
  const checksMetadataSafeParse = CheckMetadataSchema.array().safeParse(
    search.checks,
  );
  const categoriesSafeParse = CategorySchema.array().safeParse(
    search.categories,
  );

  if (checksMetadataSafeParse.success) {
    return {
      checks: checksMetadataSafeParse.data,
    };
  }

  if (categoriesSafeParse.success) {
    return {
      categories: categoriesSafeParse.data,
    };
  }

  return {
    checks: [],
  };
}

function resultsPageBeforeLoad({ search }: { search: ResultsPageSearch }) {
  const { checks, categories } = search;

  if (
    (checks && checks.length === 0) ||
    (categories && categories.length === 0)
  ) {
    throw redirect({ to: "/" });
  }
}

export const Route = createFileRoute("/results")({
  validateSearch: resultsPageValidateSearch,
  beforeLoad: resultsPageBeforeLoad,
  component: ResultsPage,
});

function ResultsPage() {
  const { checks, categories } = Route.useSearch();

  return (
    <PageLayout>
      <GoBackTitle>Check Results</GoBackTitle>

      {checks && <ResultsTable checks={checks} />}
      {categories && <ResultsTable categories={categories} />}
    </PageLayout>
  );
}
