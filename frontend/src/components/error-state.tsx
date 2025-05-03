import { useQueryClient } from "@tanstack/react-query";
import { CircleX } from "lucide-react";
import { useCallback } from "react";
import { Button } from "@/components/ui/button";

interface ErrorStateProps {
  message: string;
  invalidateQueryKey: unknown[];
}

export function ErrorState({ message, invalidateQueryKey }: ErrorStateProps) {
  const queryClient = useQueryClient();

  const handleTryAgain = useCallback(() => {
    queryClient.invalidateQueries({ queryKey: invalidateQueryKey });
  }, [invalidateQueryKey, queryClient]);

  return (
    <div className="flex flex-col items-center py-8 text-red-600 md:py-12 lg:py-16">
      <CircleX className="size-12 md:size-14 lg:size-16" />

      <p className="mt-2 font-semibold md:text-lg lg:text-xl">{message}</p>

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
