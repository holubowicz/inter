import { createFileRoute } from "@tanstack/react-router";
import { TablePageLayout } from "@/components/layout/table-page-layout";
import { ChecksTable } from "@/components/table/checks-table";
import { Title } from "@/components/title";

export const Route = createFileRoute("/")({
  component: IndexPage,
});

function IndexPage() {
  return (
    <TablePageLayout>
      <Title>Check Selection</Title>

      <ChecksTable />
    </TablePageLayout>
  );
}
