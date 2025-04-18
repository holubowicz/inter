import { PropsWithChildren } from "react";
import { cn } from "@/lib/utils";

type TitleProps = {
  className?: string;
} & PropsWithChildren;

export function Title({ children, className }: TitleProps) {
  return (
    <h1
      className={cn(
        "font-extrabold capitalize md:text-xl lg:text-2xl",
        className,
      )}
    >
      {children}
    </h1>
  );
}
