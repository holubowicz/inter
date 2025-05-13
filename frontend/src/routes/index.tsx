import { createFileRoute } from "@tanstack/react-router";
import { Separator } from "@/components/ui/separator";
import { CategoriesTable } from "@/components/categories-table";
import { ChecksTable } from "@/components/checks-table";
import { Container } from "@/components/container";
import { PageLayout } from "@/components/layout/page-layout";
import { Subtitle } from "@/components/typography/subtitle";
import { Title } from "@/components/typography/title";

export const Route = createFileRoute("/")({
  component: IndexPage,
});

function IndexPage() {
  return (
    <PageLayout>
      <Title>Selection</Title>

      <Container>
        <Subtitle>Check Selection</Subtitle>
        <ChecksTable />
      </Container>

      <Separator />

      <Container>
        <Subtitle>Category Selection</Subtitle>
        <CategoriesTable />
      </Container>
    </PageLayout>
  );
}
