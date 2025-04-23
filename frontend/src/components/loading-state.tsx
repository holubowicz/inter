import { useEffect, useState } from "react";
import { LoadingSpinner } from "@/components/loading-spinner";

export function LoadingState() {
  const [dots, setDots] = useState("");

  useEffect(() => {
    const intervalId = setInterval(() => {
      setDots((prevDots) => {
        if (prevDots.length < 3) {
          return prevDots + ".";
        } else {
          return "";
        }
      });
    }, 400);

    return () => clearInterval(intervalId);
  }, []);

  return (
    <div className="flex flex-col items-center py-8 md:py-12 lg:py-16">
      <LoadingSpinner className="size-12 md:size-14 lg:size-16" />

      <p className="mt-2 font-semibold md:text-lg lg:text-xl">Loading{dots}</p>
    </div>
  );
}
