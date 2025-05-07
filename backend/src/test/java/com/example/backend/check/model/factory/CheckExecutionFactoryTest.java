package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckExecution;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.example.backend.check.model.factory.CheckExecutionFactory.createInsertCheckExecution;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckExecutionFactoryTest {

    @Test
    void createInsertCheckExecution_whenAllProvided_thenReturnsInsertCheckExecution() {
        String checkName = "check-name";
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 10;

        CheckExecution checkExecution = createInsertCheckExecution(checkName, result, executionTime);

        assertEquals(checkName, checkExecution.getCheckName());
        assertEquals(0, result.compareTo(checkExecution.getResult()));
        assertEquals(executionTime, checkExecution.getExecutionTime());
    }

}