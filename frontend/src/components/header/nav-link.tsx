// https://tanstack.com/router/latest/docs/framework/react/guide/custom-link
import { LinkComponent, createLink } from "@tanstack/react-router";
import * as React from "react";
import { cn } from "@/lib/utils";

const NavLinkComponent = React.forwardRef<
  HTMLAnchorElement,
  React.AnchorHTMLAttributes<HTMLAnchorElement>
>((props, ref) => (
  <a
    ref={ref}
    {...props}
    className={cn(
      "after:bg-primary relative after:absolute after:bottom-[-0.125rem] after:left-0 after:h-0.5 after:w-0 after:transition-all after:duration-300 after:content-[''] hover:after:w-full",
      props.className,
    )}
  />
));

const CreatedNavLinkComponent = createLink(NavLinkComponent);

export const NavLink: LinkComponent<typeof NavLinkComponent> = (props) => (
  <CreatedNavLinkComponent preload={"intent"} {...props} />
);
