export function Footer() {
  return (
    <footer className="mt-auto flex w-full justify-center bg-zinc-700 py-2 text-white">
      <p>Inter &copy; {new Date().getFullYear() ?? 2025}</p>
    </footer>
  );
}
