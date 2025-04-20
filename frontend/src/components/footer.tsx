export function Footer() {
  return (
    <footer className="bg-secondary mt-auto flex w-full justify-center border-t-2 py-3 md:py-4">
      <p>Inter &copy; {new Date().getFullYear() ?? 2025}</p>
    </footer>
  );
}
