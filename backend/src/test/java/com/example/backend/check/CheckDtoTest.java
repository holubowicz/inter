package com.example.backend.check;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckDtoTest {

    @Test
    void from_whenCheckIsCorrect_thenReturnCheckDto() {
        Check check = new Check("avg-all", "SELECT * FROM table;", null);

        CheckDto checkDto = CheckDto.from(check);

        assertEquals(check.getName(), checkDto.getName());
    }

    @Test
    void from_whenCheckIsNull_thenThrowNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            CheckDto.from(null);
        });

        String expectedMessage = "The Check is null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}