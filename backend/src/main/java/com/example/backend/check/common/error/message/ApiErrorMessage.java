package com.example.backend.check.common.error.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ApiErrorMessage {

    public static final String CHECK_METADATA_LIST_INCORRECT =
            "Check metadata list must be provided and cannot be empty";
    public static final String CHECK_METADATA_LIST_ITEM_INCORRECT =
            "Invalid item found in the list. All items must be of a check metadata type and not null nor empty any fields";

    public static final String CATEGORIES_INCORRECT = "Categories list must be provided and cannot be empty";

}
