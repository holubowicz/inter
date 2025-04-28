import { PropsWithChildren } from "react";
import { cn } from "@/lib/utils";

interface TablePageLayoutProps extends PropsWithChildren {
  className?: string;
}

export function TablePageLayout({ children, className }: TablePageLayoutProps) {
  return (
    <div
      className={cn("flex w-full max-w-7xl flex-col gap-4 md:gap-6", className)}
    >
      {children}
    </div>
  );
}
