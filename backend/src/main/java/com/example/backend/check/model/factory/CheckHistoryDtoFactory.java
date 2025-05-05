package com.example.backend.check.model.factory;

import com.example.backend.check.common.validator.CheckHistoryValidator;
import com.example.backend.check.model.CheckHistoryDto;
import com.example.backend.database.schema.CheckHistory;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckHistoryDtoFactory {

    public static CheckHistoryDto getCheckHistoryDto(CheckHistory checkHistory) {
        CheckHistoryValidator.validate(checkHistory);
        return new CheckHistoryDto(
                checkHistory.getCheckName(),
                checkHistory.getTimestamp(),
                checkHistory.getResult(),
                checkHistory.getExecutionTime()
        );
    }

}
