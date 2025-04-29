package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static com.example.backend.check.CheckErrorMessages.*;
import static org.junit.jupiter.api.Assertions.*;

class CheckDtoFactoryTest {

    @Test
    void createCheckDto_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Instant lastTimestamp = Instant.now();
        long lastExecutionTime = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createCheckDto(null, lastResult, lastTimestamp, lastExecutionTime)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_NULL));
    }

    @Test
    void createCheckDto_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Instant lastTimestamp = Instant.now();
        long lastExecutionTime = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createCheckDto(checkName, lastResult, lastTimestamp, lastExecutionTime)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_EMPTY));
    }

    @Test
    void createCheckDto_whenLastResultIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";
        Instant lastTimestamp = Instant.now();
        long lastExecutionTime = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createCheckDto(checkName, null, lastTimestamp, lastExecutionTime)
        );

        assertTrue(exception.getMessage().contains(LAST_RESULT_NULL));
    }

    @Test
    void createCheckDto_whenLastTimestampIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        long lastExecutionTime = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createCheckDto(checkName, lastResult, null, lastExecutionTime)
        );

        assertTrue(exception.getMessage().contains(LAST_TIMESTAMP_NULL));
    }

    @Test
    void createCheckDto_whenLastExecutionTimeIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Instant lastTimestamp = Instant.now();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckDtoFactory.createCheckDto(checkName, lastResult, lastTimestamp, null)
        );

        assertTrue(exception.getMessage().contains(LAST_EXECUTION_TIME_NULL));
    }

    @Test
    void createCheckDto_whenAllProvided_thenReturnCheckDto() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Instant lastTimestamp = Instant.now();
        long lastExecutionTime = 1;

        CheckDto checkDto = CheckDtoFactory.createCheckDto(checkName, lastResult, lastTimestamp, lastExecutionTime);

        assertNotNull(checkDto);
        assertEquals(checkName, checkDto.getName());
        assertEquals(lastResult, checkDto.getLastResult());
        assertEquals(lastTimestamp, checkDto.getLastTimestamp());
        assertEquals(lastExecutionTime, checkDto.getLastExecutionTime());
    }


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