import { createFileRoute } from "@tanstack/react-router";
import { ChartsGrid } from "@/components/chart/charts-grid";
import { PageLayout } from "@/components/layout/page-layout";
import { GoBackTitle } from "@/components/typography/go-back-title";

function validateCheckName(name: string): string {
  const normalized = name.trim().toLowerCase();
  if (!/^[a-z0-9-]{1,32}$/.test(normalized)) {
    throw new Error("Invalid check name");
  }
  return normalized;
}

export const Route = createFileRoute("/checks/$checkName/history")({
  params: {
    parse: (params) => ({
      checkName: validateCheckName(params.checkName),
    }),
  },
  component: CheckHistoryPage,
});

function CheckHistoryPage() {
  const { checkName } = Route.useParams();

  return (
    <PageLayout>
      <GoBackTitle className="normal-case">{checkName}</GoBackTitle>

      <ChartsGrid checkName={checkName} />
    </PageLayout>
  );
}
