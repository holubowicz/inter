import {
  Link,
  createFileRoute,
  useCanGoBack,
  useRouter,
} from "@tanstack/react-router";
import { ChevronLeft } from "lucide-react";
import { ChartsGrid } from "@/components/chart/charts-grid";
import { PageLayout } from "@/components/layout/page-layout";
import { Title } from "@/components/title";

function validateCheckName(name: string): string {
  const normalized = name.trim().toLowerCase();
  if (!/^[a-z0-9-]{1,32}$/.test(normalized)) {
    throw new Error("Invalid check name");
  }
  return normalized;
}

export const Route = createFileRoute("/checks/$checkName/history")({
  params: {
    parse: (params) => ({
      checkName: validateCheckName(params.checkName),
    }),
  },
  component: CheckHistoryPage,
});

function CheckHistoryPage() {
  const router = useRouter();
  const canGoBack = useCanGoBack();
  const { checkName } = Route.useParams();

  const handleGoBack = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    router.history.back();
    return false;
  };

  return (
    <PageLayout>
      <Title className="normal-case">
        {canGoBack && (
          <Link to="/" onClick={handleGoBack}>
            <ChevronLeft className="size-4 md:size-5 lg:size-6" />
          </Link>
        )}

        {checkName}
      </Title>

      <ChartsGrid checkName={checkName} />
    </PageLayout>
  );
}
