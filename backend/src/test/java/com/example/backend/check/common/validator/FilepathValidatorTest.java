package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.filepath.FilepathNullException;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilepathValidatorTest {

    @Test
    void validate_whenFilepathIsNull_thenThrowsFilepathNullException() {
        assertThrows(FilepathNullException.class, () ->
                FilepathValidator.validate(null)
        );
    }

    @Test
    void validate_whenFilepathProvided_thenDoesNotThrowException() {
        assertDoesNotThrow(() ->
                FilepathValidator.validate(Paths.get("/some/path"))
        );
    }

}