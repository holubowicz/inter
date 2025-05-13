import { PropsWithChildren } from "react";
import { cn } from "@/lib/utils";

interface SubtitleProps extends PropsWithChildren {
  className?: string;
}

export function Subtitle({ children, className }: SubtitleProps) {
  return (
    <h2
      className={cn(
        "font-semibold capitalize md:text-lg lg:text-xl lg:font-bold",
        className,
      )}
    >
      {children}
    </h2>
  );
}
