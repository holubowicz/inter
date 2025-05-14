import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { PropsWithChildren, useEffect, useMemo } from "react";
import { ErrorState } from "@/components/error-state";
import { LoadingState } from "@/components/loading-state";
import { Subtitle } from "@/components/typography/subtitle";
import { getCheckHistories } from "@/lib/api/checks";
import { ChartSection } from "./chart-section";
import { ExecutionTimeChart } from "./execution-time-chart";
import { ResultChart } from "./result-chart";

interface ChartsGridProps extends PropsWithChildren {
  checkName: string;
}

const CHECK_HISTORIES_KEY = "checkHistories";

export function ChartsGrid({ checkName }: ChartsGridProps) {
  const navigate = useNavigate();
  const QUERY_KEY = useMemo(
    () => [CHECK_HISTORIES_KEY, checkName],
    [checkName],
  );

  const {
    isPending,
    error,
    data: checks,
  } = useQuery({
    // eslint-disable-next-line @tanstack/query/exhaustive-deps
    queryKey: QUERY_KEY,
    queryFn: () => getCheckHistories(checkName),
  });

  useEffect(() => {
    if (checks === undefined) {
      return;
    }

    if (checks.length !== 0) {
      return;
    }

    navigate({ to: "/" });
  }, [navigate, checks]);

  if (isPending) {
    return <LoadingState />;
  }

  if (error) {
    return (
      <ErrorState
        message="Failed to load check histories!"
        invalidateQueryKey={QUERY_KEY}
      />
    );
  }

  return (
    <div className="grid gap-6 md:gap-8 lg:gap-10">
      <ChartSection>
        <Subtitle className="text-center">Results</Subtitle>

        <ResultChart className="max-h-120 w-full" checks={checks} />
      </ChartSection>

      <ChartSection>
        <Subtitle className="text-center">
          Execution Times <span className="lowercase">[ms]</span>
        </Subtitle>

        <ExecutionTimeChart className="max-h-120 w-full" checks={checks} />
      </ChartSection>
    </div>
  );
}
