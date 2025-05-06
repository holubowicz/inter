package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.CheckExecutionNullException;
import com.example.backend.check.common.exception.ExecutionTimeNullException;
import com.example.backend.check.common.exception.ResultNullException;
import com.example.backend.check.common.exception.TimestampNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.database.schema.CheckExecution;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckExecutionValidatorTest {

    @Test
    void validate_whenCheckExecutionIsNull_thenThrowsCheckExecutionNullException() {
        assertThrows(CheckExecutionNullException.class, () ->
                CheckExecutionValidator.validate(null)
        );
    }

    @Test
    void validate_whenCheckNameIsNull_thenThrowsNameNullException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameNullException.class, () ->
                CheckExecutionValidator.validate(checkExecution)
        );
    }

    @Test
    void validate_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName("")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameEmptyException.class, () ->
                CheckExecutionValidator.validate(checkExecution)
        );
    }

    @Test
    void validate_whenCheckNameIsBlankSpace_thenThrowsNameEmptyException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName(" ")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameEmptyException.class, () ->
                CheckExecutionValidator.validate(checkExecution)
        );
    }

    @Test
    void validate_whenTimestampIsNull_thenThrowsTimestampNullException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName("check-name")
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(TimestampNullException.class, () ->
                CheckExecutionValidator.validate(checkExecution)
        );
    }

    @Test
    void validate_whenResultIsNull_thenThrowsResultNullException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .executionTime(10L)
                .build();

        assertThrows(ResultNullException.class, () ->
                CheckExecutionValidator.validate(checkExecution)
        );
    }

    @Test
    void validate_whenExecutionTimeIsNull_thenThrowsExecutionTimeNullException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .build();

        assertThrows(ExecutionTimeNullException.class, () ->
                CheckExecutionValidator.validate(checkExecution)
        );
    }

    @Test
    void validate_whenAllProvided_thenDoesNotThrowException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertDoesNotThrow(() ->
                CheckExecutionValidator.validate(checkExecution)
        );
    }

}