package com.example.backend.check.model.dto.factory;

import com.example.backend.check.common.validator.CheckExecutionValidator;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import com.example.backend.database.schema.CheckExecution;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckExecutionDTOFactory {

    public static CheckExecutionDTO createCheckExecutionDTO(CheckExecution checkExecution) {
        CheckExecutionValidator.validate(checkExecution);
        return new CheckExecutionDTO(
                checkExecution.getCheckName(),
                checkExecution.getTimestamp(),
                checkExecution.getResult(),
                checkExecution.getExecutionTime()
        );
    }

}
