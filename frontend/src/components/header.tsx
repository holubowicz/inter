import { Link } from "@tanstack/react-router";

export function Header() {
  return (
    <header className="flex w-full justify-center bg-zinc-700 py-4">
      <div className="w-11/12">
        <Link to="/">
          <h1 className="text-xl font-black uppercase">Inter</h1>
        </Link>
      </div>
    </header>
  );
}
