package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.CheckExecutionNullException;
import com.example.backend.check.model.CheckExecution;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckExecutionValidator {

    public static void validate(CheckExecution checkExecution) {
        if (checkExecution == null) {
            throw new CheckExecutionNullException();
        }

        NameValidator.validate(checkExecution.getCheckName());
        TimestampValidator.validate(checkExecution.getTimestamp());
        ResultValidator.validate(checkExecution.getResult());
        ExecutionTimeValidator.validate(checkExecution.getExecutionTime());
    }

}
