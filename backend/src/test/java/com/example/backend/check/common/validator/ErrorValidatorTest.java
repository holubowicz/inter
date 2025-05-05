package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.error.ErrorEmptyException;
import com.example.backend.check.common.exception.error.ErrorNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ErrorValidatorTest {

    @Test
    void validate_whenErrorIsNull_thenThrowsErrorNullException() {
        assertThrows(ErrorNullException.class, () ->
                ErrorValidator.validate(null)
        );
    }

    @Test
    void validate_whenErrorIsEmpty_thenThrowsErrorEmptyException() {
        assertThrows(ErrorEmptyException.class, () ->
                ErrorValidator.validate("")
        );
    }

    @Test
    void validate_whenErrorIsBlankSpace_thenThrowsErrorEmptyException() {
        assertThrows(ErrorEmptyException.class, () ->
                ErrorValidator.validate(" ")
        );
    }

    @Test
    void validate_whenErrorProvided_thenDoesNotThrowException() {
        assertDoesNotThrow(() ->
                ErrorValidator.validate("Error not found")
        );
    }

}