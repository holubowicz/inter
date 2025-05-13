import { PropsWithChildren } from "react";
import { cn } from "@/lib/utils";

interface ContainerProps extends PropsWithChildren {
  className?: string;
}

export function Container({ children, className }: ContainerProps) {
  return (
    <div className={cn("flex flex-col gap-4 md:gap-6", className)}>
      {children}
    </div>
  );
}
