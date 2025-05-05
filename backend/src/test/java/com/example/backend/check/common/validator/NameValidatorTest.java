package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NameValidatorTest {

    @Test
    void validate_whenNameIsNull_thenThrowsNameNullException() {
        assertThrows(NameNullException.class, () ->
                NameValidator.validate(null)
        );
    }

    @Test
    void validate_whenNameIsEmpty_thenThrowsNameEmptyException() {
        assertThrows(NameEmptyException.class, () ->
                NameValidator.validate("")
        );
    }

    @Test
    void validate_whenNameIsBlankSpace_thenThrowsNameEmptyException() {
        assertThrows(NameEmptyException.class, () ->
                NameValidator.validate(" ")
        );
    }

    @Test
    void validate_whenNameProvided_thenDoesNotThrowException() {
        assertDoesNotThrow(() ->
                NameValidator.validate("check-name")
        );
    }

}