import { PropsWithChildren } from "react";
import { cn } from "@/lib/utils";

interface TitleProps extends PropsWithChildren {
  className?: string;
}

export function Title({ children, className }: TitleProps) {
  return (
    <h1
      className={cn(
        "text-lg font-bold capitalize md:text-xl lg:text-2xl lg:font-extrabold",
        className,
      )}
    >
      {children}
    </h1>
  );
}
