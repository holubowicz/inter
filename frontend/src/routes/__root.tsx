import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { Outlet, createRootRoute } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { Footer } from "@/components/footer";
import { Header } from "@/components/header/header";

export const Route = createRootRoute({
  component: RootLayout,
});

function RootLayout() {
  return (
    <>
      <div className="flex min-h-dvh flex-col gap-4 md:gap-6">
        <Header />

        <main className="mx-auto flex w-11/12 justify-center">
          <Outlet />
        </main>

        <Footer />
      </div>

      <TanStackRouterDevtools />
      <ReactQueryDevtools buttonPosition="bottom-right" />
    </>
  );
}
