package com.example.backend.check.model.factory;

import com.example.backend.check.model.Check;
import lombok.experimental.UtilityClass;

import static com.example.backend.check.common.ErrorMessages.*;

@UtilityClass
public final class CheckFactory {

    public static Check createCheck(String checkName, String query) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY);
        }

        if (query == null) {
            throw new IllegalArgumentException(QUERY_NULL);
        }
        if (query.isEmpty()) {
            throw new IllegalArgumentException(QUERY_EMPTY);
        }

        return Check.builder().name(checkName).query(query).build();
    }

    public static Check createErrorCheck(String error) {
        if (error == null) {
            throw new IllegalArgumentException(ERROR_NULL);
        }
        if (error.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY);
        }

        return Check.builder().error(error).build();
    }

    public static Check createNameErrorCheck(String checkName, String error) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY);
        }

        if (error == null) {
            throw new IllegalArgumentException(ERROR_NULL);
        }
        if (error.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY);
        }

        return Check.builder().name(checkName).error(error).build();
    }

}
