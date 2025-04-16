import { createRootRoute, Outlet } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { Header } from "@/components/header";
import { Footer } from "@/components/footer";

export const Route = createRootRoute({
  component: RootLayout,
});

function RootLayout() {
  return (
    <>
      <div className="flex min-h-dvh flex-col gap-4 bg-zinc-900 text-white">
        <Header />

        <main className="mx-auto flex w-11/12 max-w-5xl flex-row items-center">
          <Outlet />
        </main>

        <Footer />
      </div>

      <TanStackRouterDevtools />
    </>
  );
}
