package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.query.QueryEmptyException;
import com.example.backend.check.common.exception.query.QueryNullException;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class QueryValidator {

    public static void validate(String query) {
        if (query == null) {
            throw new QueryNullException();
        }

        if (query.trim().isEmpty()) {
            throw new QueryEmptyException();
        }
    }


}
