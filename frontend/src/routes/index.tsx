import { createFileRoute } from "@tanstack/react-router";
import { Separator } from "@/components/ui/separator";
import { CategoriesTable } from "@/components/categories-table";
import { ChecksTable } from "@/components/checks-table";
import { Container } from "@/components/container";
import { PageLayout } from "@/components/layout/page-layout";
import { Subtitle } from "@/components/typography/subtitle";

export const Route = createFileRoute("/")({
  component: IndexPage,
});

function IndexPage() {
  return (
    <PageLayout>
      <Container>
        <Subtitle className="sm:text-center">Check Selection</Subtitle>
        <ChecksTable />
      </Container>

      <Separator />

      <Container>
        <Subtitle className="sm:text-center">Category Selection</Subtitle>
        <CategoriesTable />
      </Container>
    </PageLayout>
  );
}
