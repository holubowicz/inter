package com.example.backend.check.model.factory;

import com.example.backend.check.model.Check;

public class CheckFactory {

    public static final String CHECK_NAME_IS_NULL_ERROR = "Check name is null";
    public static final String CHECK_NAME_IS_EMPTY_ERROR = "Check name is empty";
    public static final String QUERY_IS_NULL_ERROR = "Query is null";
    public static final String QUERY_IS_EMPTY_ERROR = "Query is empty";
    public static final String ERROR_IS_NULL_ERROR = "Error is null";
    public static final String ERROR_IS_EMPTY_ERROR = "Error is empty";

    public static Check createCheck(String checkName, String query) {
        if (checkName == null) {
            throw new NullPointerException(CHECK_NAME_IS_NULL_ERROR);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_IS_EMPTY_ERROR);
        }

        if (query == null) {
            throw new NullPointerException(QUERY_IS_NULL_ERROR);
        }
        if (query.isEmpty()) {
            throw new IllegalArgumentException(QUERY_IS_EMPTY_ERROR);
        }

        return Check.builder().name(checkName).query(query).build();
    }

    public static Check createErrorCheck(String error) {
        if (error == null) {
            throw new NullPointerException(ERROR_IS_NULL_ERROR);
        }
        if (error.isEmpty()) {
            throw new IllegalArgumentException(ERROR_IS_EMPTY_ERROR);
        }

        return Check.builder().error(error).build();
    }

    public static Check createNameErrorCheck(String checkName, String error) {
        if (checkName == null) {
            throw new NullPointerException(CHECK_NAME_IS_NULL_ERROR);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_IS_EMPTY_ERROR);
        }

        if (error == null) {
            throw new NullPointerException(ERROR_IS_NULL_ERROR);
        }
        if (error.isEmpty()) {
            throw new IllegalArgumentException(ERROR_IS_EMPTY_ERROR);
        }

        return Check.builder().name(checkName).error(error).build();
    }

}
