package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.ExecutionTimeNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExecutionTimeValidatorTest {

    @Test
    void validate_whenExecutionTimeIsNull_thenThrowsExecutionTimeNullException() {
        assertThrows(ExecutionTimeNullException.class, () ->
                ExecutionTimeValidator.validate(null)
        );
    }

    @Test
    void validate_whenExecutionTimeProvided_thenDoesNotThrowException() {
        assertDoesNotThrow(() ->
                ExecutionTimeValidator.validate(12L)
        );
    }

}