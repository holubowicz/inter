import { PropsWithChildren } from "react";
import { cn } from "@/lib/utils";

type PageLayoutProps = {
  className?: string;
} & PropsWithChildren;

export function PageLayout({ children, className }: PageLayoutProps) {
  return (
    <div className={cn("flex w-full flex-col gap-4 md:gap-6", className)}>
      {children}
    </div>
  );
}
