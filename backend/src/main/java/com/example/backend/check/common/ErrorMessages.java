package com.example.backend.check.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ErrorMessages {

    public static final String CHECK_INPUT_DTO_INCORRECT = "CheckInputDto is incorrect";

    public static final String CHECK_INPUT_DTO_LIST_INCORRECT =
            "CheckInputDto list must be provided and cannot be empty";

    public static final String CHECK_INPUT_DTO_LIST_ITEM_INCORRECT =
            "Invalid item found in the list. All items must be of type CheckInputDto and not null nor empty any fields";

    public static final String FAILED_QUERY_DB = "Failed to query database";

    public static final String FAILED_TO_LOAD_CONTENT = "Failed to load check content";

}
