import { CheckedState } from "@radix-ui/react-checkbox";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useCallback, useEffect, useState } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import {
  CompactTableCell,
  CompactTableHead,
  Table,
  TableBody,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { ErrorState } from "@/components/error-state";
import { LoadingState } from "@/components/loading-state";
import { getCategories } from "@/lib/api/categories";

const AVAILABLE_CATEGORIES_KEY = "availableCategories";

export function CategoriesTable() {
  const navigate = useNavigate();

  const {
    isPending,
    error,
    data: categories,
  } = useQuery({
    queryKey: [AVAILABLE_CATEGORIES_KEY],
    queryFn: getCategories,
  });
  const [mainCheckbox, setMainCheckbox] = useState(false);
  const [checkboxes, setCheckboxes] = useState<boolean[]>([]);

  useEffect(() => {
    if (categories) {
      setCheckboxes(Array(categories.length).fill(false));
    }
  }, [categories]);

  const handleMainCheckboxChange = useCallback(
    (checked: CheckedState) => {
      if (!categories) {
        return;
      }

      setMainCheckbox(!!checked);
      setCheckboxes(Array(categories.length).fill(checked));
    },
    [categories],
  );

  const handleCheckboxChange = useCallback(
    (idx: number) => {
      const newCheckboxes = [...checkboxes];
      newCheckboxes[idx] = !newCheckboxes[idx];
      setCheckboxes(newCheckboxes);
    },
    [checkboxes],
  );

  const handleSubmitButtonClick = useCallback(() => {
    if (!categories) {
      return;
    }

    const selectedCategories = categories.filter((_, idx) => checkboxes[idx]);

    if (selectedCategories.length === 0) {
      toast("No Category Selected", {
        description: "Please select at least 1 category, then click execute.",
      });
      return;
    }

    handleMainCheckboxChange(false);

    navigate({
      to: "/results",
      search: {
        categories: selectedCategories,
      },
    });
  }, [navigate, categories, checkboxes, handleMainCheckboxChange]);

  if (isPending) {
    return <LoadingState />;
  }

  if (error) {
    return (
      <ErrorState
        message="Failed to load categories!"
        invalidateQueryKey={[AVAILABLE_CATEGORIES_KEY]}
      />
    );
  }

  return (
    <div className="flex flex-col gap-4 md:gap-6">
      <Table>
        <TableHeader>
          <TableRow className="*:font-bold *:capitalize">
            <CompactTableHead className="w-8">
              <div className="flex items-center justify-center">
                <Checkbox
                  className="cursor-pointer"
                  checked={mainCheckbox}
                  onCheckedChange={handleMainCheckboxChange}
                />
              </div>
            </CompactTableHead>

            <CompactTableHead>Name</CompactTableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {categories.map((category, idx) => (
            <TableRow key={idx}>
              <CompactTableCell>
                <div className="flex items-center justify-center">
                  <Checkbox
                    className="cursor-pointer"
                    checked={checkboxes[idx] || false}
                    onCheckedChange={() => handleCheckboxChange(idx)}
                  />
                </div>
              </CompactTableCell>

              <CompactTableCell>{category}</CompactTableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Button
        className="w-full max-w-120 cursor-pointer self-center"
        onClick={handleSubmitButtonClick}
      >
        Execute Categories
      </Button>
    </div>
  );
}
