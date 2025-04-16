import { Link } from "@tanstack/react-router";
import { ComponentProps } from "react";
import { cn } from "@/lib/utils";

type NavLinkProps = ComponentProps<typeof Link>;

export function NavLink({ ...props }: NavLinkProps) {
  return (
    <Link
      {...props}
      className={cn(
        "after:bg-primary relative after:absolute after:bottom-[-0.125rem] after:left-0 after:h-0.5 after:w-0 after:transition-all after:duration-300 after:content-[''] hover:after:w-full",
        props.className,
      )}
    />
  );
}
