package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.CheckHistoryNullException;
import com.example.backend.database.schema.CheckHistory;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckHistoryValidator {

    public static void validate(CheckHistory checkHistory) {
        if (checkHistory == null) {
            throw new CheckHistoryNullException();
        }

        NameValidator.validate(checkHistory.getCheckName());
        TimestampValidator.validate(checkHistory.getTimestamp());
        ResultValidator.validate(checkHistory.getResult());
        ExecutionTimeValidator.validate(checkHistory.getExecutionTime());
    }

}
