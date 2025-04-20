import { createFileRoute } from "@tanstack/react-router";
import { PageLayout } from "@/components/layouts/page-layout";
import { QueriesTable } from "@/components/queries-table/queries-table";
import { Title } from "@/components/title";

export const Route = createFileRoute("/")({
  component: IndexPage,
});

function IndexPage() {
  return (
    <PageLayout>
      <Title>Query Selection</Title>

      <QueriesTable />
    </PageLayout>
  );
}
