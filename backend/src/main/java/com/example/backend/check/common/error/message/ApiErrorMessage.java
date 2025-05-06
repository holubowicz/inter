package com.example.backend.check.common.error.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ApiErrorMessage {

    public static final String CHECK_INPUT_DTO_LIST_INCORRECT =
            "Check input DTO list must be provided and cannot be empty";
    public static final String CHECK_INPUT_DTO_LIST_ITEM_INCORRECT =
            "Invalid item found in the list. All items must be of a check input DTO type and not null nor empty any fields";

}
