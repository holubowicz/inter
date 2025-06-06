import { Link, useCanGoBack, useRouter } from "@tanstack/react-router";
import { ChevronLeft } from "lucide-react";
import { PropsWithChildren } from "react";
import { Title } from "@/components/typography/title";
import { cn } from "@/lib/utils";

interface GoBackTitleProps extends PropsWithChildren {
  className?: string;
}

export function GoBackTitle({ children, className }: GoBackTitleProps) {
  const router = useRouter();
  const canGoBack = useCanGoBack();

  const handleGoBack: React.MouseEventHandler<HTMLAnchorElement> = (e) => {
    e.preventDefault();
    router.history.back();
    return false;
  };

  return (
    <Title
      className={cn("flex items-center gap-1 md:gap-1.5 lg:gap-2", className)}
    >
      {canGoBack && (
        <Link to="/" onClick={handleGoBack}>
          <ChevronLeft className="size-4 md:size-5 lg:size-6" />
        </Link>
      )}

      <span>{children}</span>
    </Title>
  );
}
