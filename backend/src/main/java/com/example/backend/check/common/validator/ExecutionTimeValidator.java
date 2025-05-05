package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.ExecutionTimeNullException;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ExecutionTimeValidator {

    public static void validate(Long executionTime) {
        if (executionTime == null) {
            throw new ExecutionTimeNullException();
        }
    }

}
