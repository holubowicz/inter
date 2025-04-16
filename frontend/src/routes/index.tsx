import { createFileRoute } from "@tanstack/react-router";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { PageLayout } from "@/components/layouts/page-layout";
import { Title } from "@/components/title";

export const Route = createFileRoute("/")({
  component: IndexPage,
});

function IndexPage() {
  const queries = [
    { name: "Some query", status: "✅", available: true },
    { name: "Some query", status: "✅", available: true },
    { name: "Some query", status: "✅", available: true },
    { name: "Some query", status: "❌", available: false },
  ];

  return (
    <PageLayout>
      <Title>Query selection</Title>

      <Table>
        <TableCaption>List of available queries</TableCaption>

        <TableHeader>
          <TableRow className="*:text-center *:font-bold">
            <TableHead>Selection</TableHead>

            <TableHead>Query</TableHead>

            <TableHead>Status</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {queries.map(({ name, status, available }) => (
            <TableRow className="*:text-center">
              <TableCell className="flex items-center justify-center">
                <Checkbox className="cursor-pointer" disabled={!available} />
              </TableCell>

              <TableCell>{name}</TableCell>

              <TableCell>{status}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Button className="cursor-pointer" onClick={() => alert("click")}>
        Execute
      </Button>
    </PageLayout>
  );
}
