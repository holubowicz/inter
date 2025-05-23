package com.example.backend.check.model.dto.factory;

import com.example.backend.check.model.CheckExecution;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckExecutionDTOFactory {

    public static CheckExecutionDTO createCheckExecutionDTO(CheckExecution checkExecution) {
        return new CheckExecutionDTO(
                checkExecution.getResult(),
                checkExecution.getExecutionTime(),
                checkExecution.getTimestamp()
        );
    }

}
