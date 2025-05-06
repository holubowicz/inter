package com.example.backend.check.model.factory;

import com.example.backend.check.common.validator.CheckHistoryValidator;
import com.example.backend.check.model.CheckHistoryDTO;
import com.example.backend.database.schema.CheckHistory;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckHistoryDTOFactory {

    public static CheckHistoryDTO getCheckHistoryDTO(CheckHistory checkHistory) {
        CheckHistoryValidator.validate(checkHistory);
        return new CheckHistoryDTO(
                checkHistory.getCheckName(),
                checkHistory.getTimestamp(),
                checkHistory.getResult(),
                checkHistory.getExecutionTime()
        );
    }

}
