package com.example.backend.check.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckDtoTest {

    @Test
    void from_whenCheckIsCorrect_thenReturnCheckDto() {
        Check check = new Check("avg-all", "SELECT * FROM table;", null);

        CheckDto checkDto = CheckDto.from(check);

        assertEquals(check.getName(), checkDto.getName());
    }

    @Test
    void from_whenCheckIsNull_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            CheckDto.from(null);
        });
    }

}