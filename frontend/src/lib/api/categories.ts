import { Category, CategorySchema } from "@/types/categories";
import { CheckResult, CheckResultSchema } from "@/types/checks";
import { getApiUrl } from ".";

export async function getCategories(): Promise<Category[]> {
  const res = await fetch(getApiUrl("checks/categories"));

  if (!res.ok) {
    throw new Error(
      `Failed to fetch categories: ${res.status} ${res.statusText} - ${await res.text()}`,
    );
  }

  const data = await res.json();
  return CategorySchema.array().parse(data);
}

export async function runCategories(
  categories: Category[],
): Promise<CheckResult[]> {
  const res = await fetch(getApiUrl("checks/categories/run"), {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(categories),
  });

  if (!res.ok) {
    throw new Error(
      `Failed to run categories: ${res.status} ${res.statusText} - ${await res.text()}`,
    );
  }

  const data = await res.json();
  return CheckResultSchema.array().parse(data);
}
