import { useQueryClient } from "@tanstack/react-query";
import { CircleX } from "lucide-react";
import { Button } from "@/components/ui/button";
import { ALL_QUERIES_KEY } from "./queries-table";

export function QueriesTableError() {
  const queryClient = useQueryClient();

  const handleTryAgain = () => {
    queryClient.invalidateQueries({ queryKey: [ALL_QUERIES_KEY] });
  };

  return (
    <div className="text-destructive flex flex-col items-center py-8 md:py-12 lg:py-16">
      <CircleX className="size-12 md:size-14 lg:size-16" />

      <p className="mt-2 font-semibold md:text-lg lg:text-xl">
        Failed to load queries!
      </p>

      <Button
        className="mt-6 cursor-pointer"
        variant="secondary"
        onClick={handleTryAgain}
      >
        Try again?
      </Button>
    </div>
  );
}
