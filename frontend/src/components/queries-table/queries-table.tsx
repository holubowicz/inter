import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
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
import { QueriesTableError } from "./queries-table-error";
import { QueriesTableLoading } from "./queries-table-loading";

interface QueryApiElement {
  name: string;
}

type QueryApiResponse = QueryApiElement[];

async function fetchAllAvailableQueries(): Promise<QueryApiResponse> {
  const res = await fetch("http://localhost:8080/api/queries");

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(
      `Failed to fetch queries: ${res.status} ${res.statusText} - ${errorText}`,
    );
  }

  return res.json();
}

export const ALL_QUERIES_KEY = "allAvailableQueries";

export function QueriesTable() {
  const {
    isPending,
    error,
    data: queries,
  } = useQuery({
    queryKey: [ALL_QUERIES_KEY],
    queryFn: fetchAllAvailableQueries,
  });

  const [checkboxes, setCheckboxes] = useState<boolean[]>([]);

  useEffect(() => {
    if (queries) {
      setCheckboxes(Array(queries.length).fill(false));
    }
  }, [queries]);

  const handleCheckboxChange = (idx: number) => {
    const newCheckboxes = [...checkboxes];
    newCheckboxes[idx] = !newCheckboxes[idx];
    setCheckboxes(newCheckboxes);
  };

  const handleSubmit = () => {
    if (!queries) {
      return;
    }

    const selectedQueries = queries.filter((_, idx) => checkboxes[idx]);

    setCheckboxes(Array(queries.length).fill(false));

    alert(
      `Selected queries: ${
        selectedQueries.length > 0
          ? selectedQueries.map((query) => query.name).join(", ")
          : "None"
      }`,
    );
  };

  if (isPending) {
    return <QueriesTableLoading />;
  }

  if (error) {
    return <QueriesTableError />;
  }

  return (
    <div className="flex flex-col gap-4 md:gap-6">
      <Table>
        <TableCaption>List of all available queries</TableCaption>

        <TableHeader>
          <TableRow className="*:text-center *:font-bold *:capitalize">
            <TableHead className="max-w-6">Selection</TableHead>

            <TableHead>Query Name</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {queries.map((query, idx) => (
            <TableRow key={idx} className="*:text-center">
              <TableCell className="flex items-center justify-center">
                <Checkbox
                  className="cursor-pointer"
                  checked={checkboxes[idx] || false}
                  onCheckedChange={() => handleCheckboxChange(idx)}
                />
              </TableCell>

              <TableCell>{query.name}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Button
        className="w-full max-w-120 cursor-pointer self-center"
        onClick={handleSubmit}
      >
        Execute
      </Button>
    </div>
  );
}
