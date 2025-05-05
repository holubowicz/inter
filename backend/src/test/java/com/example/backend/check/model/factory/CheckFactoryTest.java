package com.example.backend.check.model.factory;

import com.example.backend.check.common.exception.error.ErrorEmptyException;
import com.example.backend.check.common.exception.error.ErrorNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.common.exception.query.QueryEmptyException;
import com.example.backend.check.common.exception.query.QueryNullException;
import com.example.backend.check.model.Check;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckFactoryTest {

    @Test
    void createCheck_whenCheckNameIsNull_thenThrowsNameNullException() {
        String query = "SELECT * FROM check;";

        assertThrows(NameNullException.class, () ->
                CheckFactory.createCheck(null, query)
        );
    }

    @Test
    void createCheck_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";
        String query = "SELECT * FROM check;";

        assertThrows(NameEmptyException.class, () ->
                CheckFactory.createCheck(checkName, query)
        );
    }

    @Test
    void createCheck_whenQueryIsNull_thenThrowsQueryNullException() {
        String checkName = "check-name";

        assertThrows(QueryNullException.class, () ->
                CheckFactory.createCheck(checkName, null)
        );
    }

    @Test
    void createCheck_whenQueryIsEmpty_thenThrowsQueryEmptyException() {
        String checkName = "check-name";
        String query = "";

        assertThrows(QueryEmptyException.class, () ->
                CheckFactory.createCheck(checkName, query)
        );
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
    void createErrorCheck_whenErrorIsNull_thenThrowsErrorNullException() {
        assertThrows(ErrorNullException.class, () ->
                CheckFactory.createErrorCheck(null)
        );
    }

    @Test
    void createErrorCheck_whenErrorIsEmpty_thenThrowsErrorEmptyException() {
        String error = "";

        assertThrows(ErrorEmptyException.class, () ->
                CheckFactory.createErrorCheck(error)
        );
    }

    @Test
    void createErrorCheck_whenErrorProvided_thenReturnsCheck() {
        String error = "some error";

        Check check = CheckFactory.createErrorCheck(error);

        assertNotNull(check);
        assertEquals(error, check.getError());
        assertNull(check.getName());
        assertNull(check.getQuery());
    }


    @Test
    void createNameErrorCheck_whenCheckNameIsNull_thenThrowsNameNullException() {
        String error = "some error";

        assertThrows(NameNullException.class, () ->
                CheckFactory.createNameErrorCheck(null, error)
        );
    }

    @Test
    void createNameErrorCheck_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";
        String error = "some error";

        assertThrows(NameEmptyException.class, () ->
                CheckFactory.createNameErrorCheck(checkName, error)
        );
    }

    @Test
    void createNameErrorCheck_whenErrorIsNull_thenThrowsErrorNullException() {
        String checkName = "check-name";

        assertThrows(ErrorNullException.class, () ->
                CheckFactory.createNameErrorCheck(checkName, null)
        );
    }

    @Test
    void createNameErrorCheck_whenErrorIsEmpty_thenThrowsErrorEmptyException() {
        String checkName = "check-name";
        String error = "";

        assertThrows(ErrorEmptyException.class, () ->
                CheckFactory.createNameErrorCheck(checkName, error)
        );
    }

    @Test
    void createNameErrorCheck_whenCheckNameAndErrorProvided_thenReturnsCheck() {
        String checkName = "check-name";
        String error = "some error";

        Check check = CheckFactory.createNameErrorCheck(checkName, error);

        assertNotNull(check);
        assertEquals(checkName, check.getName());
        assertEquals(error, check.getError());
        assertNull(check.getQuery());
    }

}