import { PropsWithChildren } from "react";
import { cn } from "@/lib/utils";

interface TitleProps extends PropsWithChildren {
  className?: string;
}

export function Title({ children, className }: TitleProps) {
  return (
    <h1
      className={cn(
        "flex items-center text-lg font-bold capitalize md:gap-1 md:text-xl lg:gap-1.5 lg:text-2xl lg:font-extrabold",
        className,
      )}
    >
      {children}
    </h1>
  );
}
