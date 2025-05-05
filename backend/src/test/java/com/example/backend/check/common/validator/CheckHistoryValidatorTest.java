package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.CheckHistoryNullException;
import com.example.backend.check.common.exception.ExecutionTimeNullException;
import com.example.backend.check.common.exception.ResultNullException;
import com.example.backend.check.common.exception.TimestampNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.database.schema.CheckHistory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckHistoryValidatorTest {

    @Test
    void validate_whenCheckHistoryIsNull_thenThrowsCheckHistoryNullException() {
        assertThrows(CheckHistoryNullException.class, () ->
                CheckHistoryValidator.validate(null)
        );
    }

    @Test
    void validate_whenCheckNameIsNull_thenThrowsNameNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameNullException.class, () ->
                CheckHistoryValidator.validate(checkHistory)
        );
    }

    @Test
    void validate_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameEmptyException.class, () ->
                CheckHistoryValidator.validate(checkHistory)
        );
    }

    @Test
    void validate_whenCheckNameIsBlankSpace_thenThrowsNameEmptyException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName(" ")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameEmptyException.class, () ->
                CheckHistoryValidator.validate(checkHistory)
        );
    }

    @Test
    void validate_whenTimestampIsNull_thenThrowsTimestampNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(TimestampNullException.class, () ->
                CheckHistoryValidator.validate(checkHistory)
        );
    }

    @Test
    void validate_whenResultIsNull_thenThrowsResultNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .executionTime(10L)
                .build();

        assertThrows(ResultNullException.class, () ->
                CheckHistoryValidator.validate(checkHistory)
        );
    }

    @Test
    void validate_whenExecutionTimeIsNull_thenThrowsExecutionTimeNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .build();

        assertThrows(ExecutionTimeNullException.class, () ->
                CheckHistoryValidator.validate(checkHistory)
        );
    }

    @Test
    void validate_whenAllProvided_thenDoesNotThrowException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertDoesNotThrow(() ->
                CheckHistoryValidator.validate(checkHistory)
        );
    }

}