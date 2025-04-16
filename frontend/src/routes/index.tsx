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
    { name: "Przeliczanie ludzi", status: "✅", available: true },
    { name: "Przeliczanie ludzi", status: "✅", available: true },
    { name: "Przeliczanie ludzi", status: "✅", available: true },
    { name: "Przeliczanie ludzi", status: "❌", available: false },
  ];

  return (
    <PageLayout>
      <Title>Wybór Poleceń</Title>

      <Table>
        <TableCaption>List dostępnych poleceń</TableCaption>

        <TableHeader>
          <TableRow className="*:text-center *:font-bold">
            <TableHead>Wybór</TableHead>

            <TableHead>Polecenie</TableHead>

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
        Wykonaj polecenia
      </Button>
    </PageLayout>
  );
}
