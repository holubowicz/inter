import { PropsWithChildren } from "react";
import { cn } from "@/lib/utils";

interface PageLayoutProps extends PropsWithChildren {
  className?: string;
}

export function PageLayout({ children, className }: PageLayoutProps) {
  return (
    <div className={cn("flex w-full flex-col gap-4 md:gap-6", className)}>
      {children}
    </div>
  );
}
