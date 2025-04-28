package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CheckDtoFactoryTest {

    @Test
    void createCheckDto_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Instant lastTimestamp = Instant.now();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createCheckDto(null, lastResult, lastTimestamp)
        );

        assertTrue(exception.getMessage().contains(CheckDtoFactory.CHECK_NAME_NULL_ERROR));
    }

    @Test
    void createCheckDto_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Instant lastTimestamp = Instant.now();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createCheckDto(checkName, lastResult, lastTimestamp)
        );

        assertTrue(exception.getMessage().contains(CheckDtoFactory.CHECK_NAME_EMPTY_ERROR));
    }

    @Test
    void createCheckDto_whenLastResultIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";
        Instant lastTimestamp = Instant.now();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createCheckDto(checkName, null, lastTimestamp)
        );

        assertTrue(exception.getMessage().contains(CheckDtoFactory.LAST_RESULT_NULL_ERROR));
    }

    @Test
    void createCheckDto_whenLastTimestampIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createCheckDto(checkName, lastResult, null)
        );

        assertTrue(exception.getMessage().contains(CheckDtoFactory.LAST_TIMESTAMP_NULL_ERROR));
    }

    @Test
    void createCheckDto_whenAllProvided_thenReturnCheckDto() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Instant lastTimestamp = Instant.now();

        CheckDto checkDto = CheckDtoFactory.createCheckDto(checkName, lastResult, lastTimestamp);

        assertNotNull(checkDto);
        assertEquals(checkName, checkDto.getName());
        assertEquals(lastResult, checkDto.getLastResult());
        assertEquals(lastTimestamp, checkDto.getLastTimestamp());
    }


    @Test
    void createNameCheckDto_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createNameCheckDto(null)
        );

        assertTrue(exception.getMessage().contains(CheckDtoFactory.CHECK_NAME_NULL_ERROR));
    }

    @Test
    void createNameCheckDto_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createNameCheckDto(checkName)
        );

        assertTrue(exception.getMessage().contains(CheckDtoFactory.CHECK_NAME_EMPTY_ERROR));
    }

    @Test
    void createNameCheckDto_whenCheckNameProvided_thenReturnCheckDto() {
        String checkName = "check-name";

        CheckDto checkDto = CheckDtoFactory.createNameCheckDto(checkName);

        assertNotNull(checkDto);
        assertEquals(checkName, checkDto.getName());
    }

}