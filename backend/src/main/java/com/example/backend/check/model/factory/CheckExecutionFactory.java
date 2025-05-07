package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckExecution;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public final class CheckExecutionFactory {

    public static CheckExecution createInsertCheckExecution(String checkName, BigDecimal result, Long executionTime) {
        return CheckExecution.builder()
                .checkName(checkName)
                .result(result)
                .executionTime(executionTime)
                .build();
    }

}
