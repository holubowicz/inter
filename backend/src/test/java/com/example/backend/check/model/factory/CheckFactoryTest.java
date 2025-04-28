package com.example.backend.check.model.factory;

import com.example.backend.check.model.Check;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckFactoryTest {

    @Test
    void createCheck_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        String query = "SELECT * FROM check;";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createCheck(null, query)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.CHECK_NAME_NULL_ERROR));
    }

    @Test
    void createCheck_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";
        String query = "SELECT * FROM check;";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createCheck(checkName, query)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.CHECK_NAME_EMPTY_ERROR));
    }

    @Test
    void createCheck_whenQueryIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createCheck(checkName, null)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.QUERY_NULL_ERROR));
    }

    @Test
    void createCheck_whenQueryIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "check-name";
        String query = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createCheck(checkName, query)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.QUERY_EMPTY_ERROR));
    }

    @Test
    void createCheck_whenCheckNameAndQueryProvided_thenReturnCheck() {
        String checkName = "check-name";
        String query = "SELECT * FROM check;";

        Check check = CheckFactory.createCheck(checkName, query);

        assertNotNull(check);
        assertEquals(checkName, check.getName());
        assertEquals(query, check.getQuery());
        assertNull(check.getError());
    }


    @Test
    void createErrorCheck_whenErrorIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createErrorCheck(null)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.ERROR_NULL_ERROR));
    }

    @Test
    void createErrorCheck_whenErrorIsEmpty_thenThrowIllegalArgumentException() {
        String error = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createErrorCheck(error)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.ERROR_EMPTY_ERROR));
    }

    @Test
    void createErrorCheck_whenErrorProvided_thenReturnCheck() {
        String error = "some error";

        Check check = CheckFactory.createErrorCheck(error);

        assertNotNull(check);
        assertEquals(error, check.getError());
        assertNull(check.getName());
        assertNull(check.getQuery());
    }


    @Test
    void createNameErrorCheck_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        String error = "some error";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createNameErrorCheck(null, error)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.CHECK_NAME_NULL_ERROR));
    }

    @Test
    void createNameErrorCheck_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";
        String error = "some error";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createNameErrorCheck(checkName, error)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.CHECK_NAME_EMPTY_ERROR));
    }

    @Test
    void createNameErrorCheck_whenErrorIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createNameErrorCheck(checkName, null)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.ERROR_NULL_ERROR));
    }

    @Test
    void createNameErrorCheck_whenErrorIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "check-name";
        String error = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckFactory.createNameErrorCheck(checkName, error)
        );

        assertTrue(exception.getMessage().contains(CheckFactory.ERROR_EMPTY_ERROR));
    }

    @Test
    void createNameErrorCheck_whenCheckNameAndErrorProvided_thenReturnCheck() {
        String checkName = "check-name";
        String error = "some error";

        Check check = CheckFactory.createNameErrorCheck(checkName, error);

        assertNotNull(check);
        assertEquals(checkName, check.getName());
        assertEquals(error, check.getError());
        assertNull(check.getQuery());
    }

}