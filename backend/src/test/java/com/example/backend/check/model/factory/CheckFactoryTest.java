package com.example.backend.check.model.factory;

import com.example.backend.check.model.Check;
import org.junit.jupiter.api.Test;

import static com.example.backend.check.model.factory.CheckFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class CheckFactoryTest {

    @Test
    void createCheck_whenCheckNameAndQueryProvided_thenReturnCheck() {
        String checkName = "check-name";
        String query = "SELECT * FROM check";

        Check check = createCheck(checkName, query);

        assertNotNull(check);
        assertEquals(checkName, check.getName());
        assertEquals(query, check.getQuery());
        assertNull(check.getError());
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