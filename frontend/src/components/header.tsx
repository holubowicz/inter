import { Link } from "@tanstack/react-router";
import { ThemeToggle } from "@/components/theme-toggle";

export function Header() {
  return (
    <header className="bg-secondary flex w-full justify-center py-4">
      <div className="flex w-11/12 max-w-7xl items-center justify-between">
        <nav>
          <Link to="/">
            <span className="text-xl font-black uppercase">Inter</span>
          </Link>
        </nav>

        <ThemeToggle />
      </div>
    </header>
  );
}
