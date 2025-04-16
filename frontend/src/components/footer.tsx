export function Footer() {
  return (
    <footer className="bg-secondary mt-auto flex w-full justify-center py-3">
      <p>Inter &copy; {new Date().getFullYear() ?? 2025}</p>
    </footer>
  );
}
