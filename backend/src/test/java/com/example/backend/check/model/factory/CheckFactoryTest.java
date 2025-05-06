package com.example.backend.check.model.factory;

import com.example.backend.check.common.exception.error.ErrorEmptyException;
import com.example.backend.check.common.exception.error.ErrorNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.common.exception.query.QueryEmptyException;
import com.example.backend.check.common.exception.query.QueryNullException;
import com.example.backend.check.model.Check;
import org.junit.jupiter.api.Test;

import static com.example.backend.check.model.factory.CheckFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class CheckFactoryTest {

    @Test
    void createCheck_whenCheckNameIsNull_thenThrowsNameNullException() {
        String query = "SELECT * FROM check;";

        assertThrows(NameNullException.class, () -> createCheck(null, query));
    }

    @Test
    void createCheck_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";
        String query = "SELECT * FROM check;";

        assertThrows(NameEmptyException.class, () -> createCheck(checkName, query));
    }

    @Test
    void createCheck_whenQueryIsNull_thenThrowsQueryNullException() {
        String checkName = "check-name";

        assertThrows(QueryNullException.class, () -> createCheck(checkName, null));
    }

    @Test
    void createCheck_whenQueryIsEmpty_thenThrowsQueryEmptyException() {
        String checkName = "check-name";
        String query = "";

        assertThrows(QueryEmptyException.class, () -> createCheck(checkName, query));
    }

    @Test
    void createCheck_whenCheckNameAndQueryProvided_thenReturnCheck() {
        String checkName = "check-name";
        String query = "SELECT * FROM check;";

        Check check = createCheck(checkName, query);

        assertNotNull(check);
        assertEquals(checkName, check.getName());
        assertEquals(query, check.getQuery());
        assertNull(check.getError());
    }


    @Test
    void createErrorCheck_whenErrorIsNull_thenThrowsErrorNullException() {
        assertThrows(ErrorNullException.class, () -> createErrorCheck(null));
    }

    @Test
    void createErrorCheck_whenErrorIsEmpty_thenThrowsErrorEmptyException() {
        String error = "";

        assertThrows(ErrorEmptyException.class, () -> createErrorCheck(error));
    }

    @Test
    void createErrorCheck_whenErrorProvided_thenReturnsCheck() {
        String error = "some error";

        Check check = createErrorCheck(error);

        assertNotNull(check);
        assertEquals(error, check.getError());
        assertNull(check.getName());
        assertNull(check.getQuery());
    }


    @Test
    void createNameErrorCheck_whenCheckNameIsNull_thenThrowsNameNullException() {
        String error = "some error";

        assertThrows(NameNullException.class, () -> createNameErrorCheck(null, error));
    }

    @Test
    void createNameErrorCheck_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";
        String error = "some error";

        assertThrows(NameEmptyException.class, () -> createNameErrorCheck(checkName, error));
    }

    @Test
    void createNameErrorCheck_whenErrorIsNull_thenThrowsErrorNullException() {
        String checkName = "check-name";

        assertThrows(ErrorNullException.class, () -> createNameErrorCheck(checkName, null));
    }

    @Test
    void createNameErrorCheck_whenErrorIsEmpty_thenThrowsErrorEmptyException() {
        String checkName = "check-name";
        String error = "";

        assertThrows(ErrorEmptyException.class, () -> createNameErrorCheck(checkName, error));
    }

    @Test
    void createNameErrorCheck_whenCheckNameAndErrorProvided_thenReturnsCheck() {
        String checkName = "check-name";
        String error = "some error";

        Check check = createNameErrorCheck(checkName, error);

        assertNotNull(check);
        assertEquals(checkName, check.getName());
        assertEquals(error, check.getError());
        assertNull(check.getQuery());
    }

}