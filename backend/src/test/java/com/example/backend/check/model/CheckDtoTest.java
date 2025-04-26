package com.example.backend.check.model;

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
    void from_whenCheckIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CheckDto.from(null);
        });

        assertTrue(exception.getMessage().contains(CheckDto.CHECK_NULL_ERROR));
    }

}