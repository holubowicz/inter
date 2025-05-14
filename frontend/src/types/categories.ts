import { z } from "zod";

// CategoryDTO

export const CategoryDTOSchema = z.string();

export type CategoryDTO = z.infer<typeof CategoryDTOSchema>;

// Category

export const CategorySchema = z.object({
  name: z.string(),
  count: z.number(),
});

export type Category = z.infer<typeof CategorySchema>;
