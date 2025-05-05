package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.query.QueryEmptyException;
import com.example.backend.check.common.exception.query.QueryNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueryValidatorTest {

    @Test
    void validate_whenQueryIsNull_thenThrowsQueryNullException() {
        assertThrows(QueryNullException.class, () ->
                QueryValidator.validate(null)
        );
    }

    @Test
    void validate_whenQueryIsEmpty_thenThrowsQueryEmptyException() {
        assertThrows(QueryEmptyException.class, () ->
                QueryValidator.validate("")
        );
    }

    @Test
    void validate_whenQueryIsBlankSpace_thenThrowsQueryEmptyException() {
        assertThrows(QueryEmptyException.class, () ->
                QueryValidator.validate(" ")
        );
    }

    @Test
    void validate_whenQueryProvided_thenDoesNotThrowException() {
        assertDoesNotThrow(() ->
                QueryValidator.validate("SELECT * FROM test")
        );
    }

}