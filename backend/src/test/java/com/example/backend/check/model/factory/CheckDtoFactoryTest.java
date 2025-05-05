package com.example.backend.check.model.factory;

import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.CheckDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckDtoFactoryTest {

    @Test
    void createNameCheckDto_whenCheckNameIsNull_thenThrowsNameNullException() {
        assertThrows(NameNullException.class, () ->
                CheckDtoFactory.createNameCheckDto(null)
        );
    }

    @Test
    void createNameCheckDto_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";

        assertThrows(NameEmptyException.class, () ->
                CheckDtoFactory.createNameCheckDto(checkName)
        );
    }

    @Test
    void createNameCheckDto_whenCheckNameProvided_thenReturnsCheckDto() {
        String checkName = "check-name";

        CheckDto checkDto = CheckDtoFactory.createNameCheckDto(checkName);

        assertNotNull(checkDto);
        assertEquals(checkName, checkDto.getName());
    }

}