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
  const [checkboxes, setCheckboxes] = useState<boolean[]>([]);

  useEffect(() => {
    if (categories) {
      setCheckboxes(Array(categories.length).fill(false));
    }
  }, [categories]);

  const handleAllCheckboxesChange = useCallback(
    (checked: boolean) => {
      if (!categories) {
        return;
      }

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

    // TODO: fix the main checkbox is not unchecked
    setCheckboxes(Array(categories.length).fill(false));

    // TODO: navigate to results page
    // navigate({
    //   to: "/results",
    //   search: {
    //     checks: selectedCategories,
    //   },
    // });
  }, [categories, checkboxes, navigate]);

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
          <TableRow className="*:text-center *:font-bold *:capitalize">
            <CompactTableHead className="max-w-6">
              <div className="flex items-center justify-center">
                <Checkbox
                  className="cursor-pointer"
                  onCheckedChange={(checked) =>
                    handleAllCheckboxesChange(!!checked)
                  }
                />
              </div>
            </CompactTableHead>

            <CompactTableHead>Name</CompactTableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {categories.map((category, idx) => (
            <TableRow key={idx} className="*:text-center">
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
