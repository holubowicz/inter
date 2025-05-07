package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.NameNullOrEmptyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NameValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    void validate_whenNameIsNullOrEmpty_thenThrowsThrowsNameNullOrEmptyException(String name) {
        assertThrows(NameNullOrEmptyException.class, () ->
                NameValidator.validate(name)
        );
    }

    @Test
    void validate_whenNameProvided_thenDoesNotThrowException() {
        assertDoesNotThrow(() ->
                NameValidator.validate("check-name")
        );
    }

}