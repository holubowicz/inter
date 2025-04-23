import { createFileRoute } from "@tanstack/react-router";
import { PageLayout } from "@/components/layout/page-layout";
import { ChecksTable } from "@/components/table/checks-table";
import { Title } from "@/components/title";

export const Route = createFileRoute("/")({
  component: IndexPage,
});

function IndexPage() {
  return (
    <PageLayout>
      <Title>Check Selection</Title>

      <ChecksTable />
    </PageLayout>
  );
}
