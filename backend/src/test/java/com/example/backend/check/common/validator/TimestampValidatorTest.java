package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.TimestampNullException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimestampValidatorTest {

    @Test
    void validate_whenTimestampIsNull_thenThrowsTimestampNullException() {
        assertThrows(TimestampNullException.class, () ->
                TimestampValidator.validate(null)
        );
    }

    @Test
    void validate_whenTimestampProvided_thenDoesNotThrowException() {
        assertDoesNotThrow(() ->
                TimestampValidator.validate(Instant.now())
        );
    }

}