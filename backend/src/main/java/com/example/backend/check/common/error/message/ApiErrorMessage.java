package com.example.backend.check.common.error.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ApiErrorMessage {

    public static final String CHECK_INPUT_DTO_LIST_INCORRECT =
            "CheckInputDto list must be provided and cannot be empty";
    public static final String CHECK_INPUT_DTO_LIST_ITEM_INCORRECT =
            "Invalid item found in the list. All items must be of type CheckInputDto and not null nor empty any fields";

}
