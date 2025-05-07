package com.example.backend.check.model.factory;

import com.example.backend.check.common.exception.ExecutionTimeNullException;
import com.example.backend.check.common.exception.ResultNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.CheckExecution;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.example.backend.check.model.factory.CheckExecutionFactory.createInsertCheckExecution;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckExecutionFactoryTest {

    @Test
    void createInsertCheckExecution_whenCheckNameIsNull_thenThrowsNameNullException() {
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 10;

        assertThrows(NameNullException.class, () ->
                createInsertCheckExecution(null, result, executionTime)
        );
    }

    @Test
    void createInsertCheckExecution_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 10;

        assertThrows(NameEmptyException.class, () ->
                createInsertCheckExecution(checkName, result, executionTime)
        );
    }

    @Test
    void createInsertCheckExecution_whenCheckNameIsBlankSpace_thenThrowsNameEmptyException() {
        String checkName = " ";
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 10;

        assertThrows(NameEmptyException.class, () ->
                createInsertCheckExecution(checkName, result, executionTime)
        );
    }

    @Test
    void createInsertCheckExecution_whenResultIsNull_thenThrowsResultNullException() {
        String checkName = "check-name";
        long executionTime = 10;

        assertThrows(ResultNullException.class, () ->
                createInsertCheckExecution(checkName, null, executionTime)
        );
    }

    @Test
    void createInsertCheckExecution_whenExecutionTimeIsNull_thenThrowsExecutionTimeNullException() {
        String checkName = "check-name";
        BigDecimal result = BigDecimal.valueOf(10);

        assertThrows(ExecutionTimeNullException.class, () ->
                createInsertCheckExecution(checkName, result, null)
        );
    }

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