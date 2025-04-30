import { createFileRoute } from "@tanstack/react-router";
import { ChecksTable } from "@/components/checks-table";
import { PageLayout } from "@/components/layout/page-layout";
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
