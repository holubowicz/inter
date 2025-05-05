package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckDto;
import org.junit.jupiter.api.Test;

import static com.example.backend.check.common.ErrorMessages.CHECK_NAME_EMPTY;
import static com.example.backend.check.common.ErrorMessages.CHECK_NAME_NULL;
import static org.junit.jupiter.api.Assertions.*;

class CheckDtoFactoryTest {

    @Test
    void createNameCheckDto_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createNameCheckDto(null)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_NULL));
    }

    @Test
    void createNameCheckDto_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createNameCheckDto(checkName)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_EMPTY));
    }

    @Test
    void createNameCheckDto_whenCheckNameProvided_thenReturnCheckDto() {
        String checkName = "check-name";

        CheckDto checkDto = CheckDtoFactory.createNameCheckDto(checkName);

        assertNotNull(checkDto);
        assertEquals(checkName, checkDto.getName());
    }

}