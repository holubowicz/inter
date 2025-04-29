package com.example.backend.check;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckErrorMessages {

    public static final String CHECK_DIRECTORY_DONT_EXIST = "Check directory does not exist";

    public static final String CHECK_DTO_LIST_NULL = "Check DTO list is null";

    public static final String CHECK_INPUT_DTO_INCORRECT = "CheckInputDto is incorrect";
    public static final String CHECK_INPUT_DTO_LIST_INCORRECT =
            "CheckInputDto list must be provided and cannot be empty";
    public static final String CHECK_INPUT_DTO_LIST_ITEM_INCORRECT =
            "Invalid item found in the list. All items must be of type CheckInputDto and not null nor empty any fields";
    public static final String CHECK_INPUT_DTO_NULL = "CheckInputDto is null";

    public static final String CHECK_NAME_EMPTY = "Check name is empty";
    public static final String CHECK_NAME_NULL = "Check name is null";

    public static final String CHECK_NULL = "Check is null";

    public static final String ERROR_EMPTY = "Error is empty";
    public static final String ERROR_NULL = "Error is null";

    public static final String EXECUTION_TIME_NULL = "Execution time is null";

    public static final String FAILED_QUERY_DB = "Failed to query database";

    public static final String FAILED_TO_LOAD_CHECKS = "Failed to load checks";
    public static final String FAILED_TO_LOAD_CONTENT = "Failed to load check content";

    public static final String FILEPATH_EMPTY = "File path is empty";
    public static final String FILEPATH_NULL = "File path is null";

    public static final String LAST_EXECUTION_TIME_NULL = "Last execution time is null";

    public static final String LAST_RESULT_NULL = "Last result is null";

    public static final String LAST_TIMESTAMP_NULL = "Last timestamp is null";

    public static final String QUERY_EMPTY = "Query is empty";
    public static final String QUERY_NULL = "Query is null";

    public static final String RESULT_NULL = "Result is null";
}
