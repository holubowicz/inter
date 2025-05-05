package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckHistoryDto;
import com.example.backend.database.schema.CheckHistory;
import lombok.experimental.UtilityClass;

import static com.example.backend.check.common.ErrorMessages.*;

@UtilityClass
public final class CheckHistoryDtoFactory {

    public static CheckHistoryDto getCheckHistoryDto(CheckHistory checkHistory) {
        if (checkHistory == null) {
            throw new IllegalArgumentException(CHECK_HISTORY_NULL);
        }

        if (checkHistory.getCheckName() == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL);
        }
        if (checkHistory.getCheckName().isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY);
        }

        if (checkHistory.getTimestamp() == null) {
            throw new IllegalArgumentException(TIMESTAMP_NULL);
        }

        if (checkHistory.getResult() == null) {
            throw new IllegalArgumentException(RESULT_NULL);
        }

        if (checkHistory.getExecutionTime() == null) {
            throw new IllegalArgumentException(EXECUTION_TIME_NULL);
        }

        return new CheckHistoryDto(
                checkHistory.getCheckName(),
                checkHistory.getTimestamp(),
                checkHistory.getResult(),
                checkHistory.getExecutionTime()
        );
    }

}
