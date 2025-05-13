import { z } from "zod";

// Category

export const CategorySchema = z.string();

export type Category = z.infer<typeof CategorySchema>;
