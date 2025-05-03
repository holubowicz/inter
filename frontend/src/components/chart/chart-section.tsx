import { PropsWithChildren } from "react";

export function ChartSection({ children }: PropsWithChildren) {
  return (
    <section className="flex w-full flex-col gap-2 md:gap-4">
      {children}
    </section>
  );
}
