package com.example.backend.check.model.factory;

import com.example.backend.check.common.exception.error.ErrorEmptyException;
import com.example.backend.check.common.exception.error.ErrorNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.CheckResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckResultFactoryTest {

    @Test
    void createNameErrorCheckResult_whenNameIsNull_thenThrowsNameNullException() {
        String error = "Some error";

        assertThrows(NameNullException.class, () ->
                CheckResultFactory.createNameErrorCheckResult(null, error)
        );
    }

    @Test
    void createNameErrorCheckResult_whenNameIsEmpty_thenThrowsNameEmptyException() {
        String name = "";
        String error = "Some error";

        assertThrows(NameEmptyException.class, () ->
                CheckResultFactory.createNameErrorCheckResult(name, error)
        );
    }

    @Test
    void createNameErrorCheckResult_whenNameIsBlankSpace_thenThrowsNameEmptyException() {
        String name = " ";
        String error = "Some error";

        assertThrows(NameEmptyException.class, () ->
                CheckResultFactory.createNameErrorCheckResult(name, error)
        );
    }

    @Test
    void createNameErrorCheckResult_whenErrorIsNull_thenThrowsErrorNullException() {
        String name = "check-name";

        assertThrows(ErrorNullException.class, () ->
                CheckResultFactory.createNameErrorCheckResult(name, null)
        );
    }

    @Test
    void createNameErrorCheckResult_whenErrorIsEmpty_thenThrowsErrorEmptyException() {
        String name = "check-name";
        String error = "";

        assertThrows(ErrorEmptyException.class, () ->
                CheckResultFactory.createNameErrorCheckResult(name, error)
        );
    }

    @Test
    void createNameErrorCheckResult_whenErrorIsBlankSpace_thenThrowsErrorEmptyException() {
        String name = "check-name";
        String error = " ";

        assertThrows(ErrorEmptyException.class, () ->
                CheckResultFactory.createNameErrorCheckResult(name, error)
        );
    }

    @Test
    void createNameErrorCheckResult_whenAllProvided_thenReturnsCheckResult() {
        String name = "check-name";
        String error = "Some error";

        CheckResult checkResult = CheckResultFactory.createNameErrorCheckResult(name, error);

        assertEquals(name, checkResult.getName());
        assertEquals(error, checkResult.getError());
    }

}