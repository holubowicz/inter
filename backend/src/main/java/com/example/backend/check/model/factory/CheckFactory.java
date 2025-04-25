package com.example.backend.check.model.factory;

import com.example.backend.check.model.Check;

public class CheckFactory {

    public static final String CHECK_DTO_INCORRECT_ERROR = "Check DTO is incorrect";
    public static final String FAILED_TO_LOAD_CONTENT_ERROR = "Failed to load check content";

    public static Check createCheck(String checkName, String query) {
        return Check.builder().name(checkName).query(query).build();
    }

    public static Check createErrorCheck(String error) {
        return Check.builder().error(error).build();
    }

    public static Check createNameErrorCheck(String checkName, String error) {
        return Check.builder().name(checkName).error(error).build();
    }

}
