import { NavLink } from "@/components/header/nav-link";
import { ThemeToggle } from "@/components/theme/theme-toggle";

export function Header() {
  return (
    <header className="bg-secondary flex w-full justify-center py-4">
      <div className="flex w-11/12 max-w-7xl items-center justify-between">
        <nav>
          <NavLink to="/" className="text-xl font-black uppercase">
            Inter
          </NavLink>
        </nav>

        <ThemeToggle />
      </div>
    </header>
  );
}
