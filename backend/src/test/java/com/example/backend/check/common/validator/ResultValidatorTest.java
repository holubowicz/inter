package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.ResultNullException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResultValidatorTest {

    @Test
    void validate_whenResultIsNull_thenThrowsResultNullException() {
        assertThrows(ResultNullException.class, () ->
                ResultValidator.validate(null)
        );
    }

    @Test
    void validate_whenResultProvided_thenDoesNotThrowException() {
        assertDoesNotThrow(() ->
                ResultValidator.validate(BigDecimal.valueOf(1))
        );
    }

}