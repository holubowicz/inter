package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckHistoryDto;
import com.example.backend.database.schema.CheckHistory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static com.example.backend.check.CheckErrorMessages.*;
import static org.junit.jupiter.api.Assertions.*;

class CheckHistoryDtoFactoryTest {

    @Test
    void getCheckHistoryDto_whenCheckHistoryIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(null)
        );

        assertTrue(exception.getMessage().contains(CHECK_HISTORY_NULL));
    }

    @Test
    void getCheckHistoryDto_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .timestamp(Instant.now())
                        .result(BigDecimal.valueOf(10))
                        .executionTime(10L)
                        .build()
                )
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_NULL));
    }

    @Test
    void getCheckHistoryDto_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .checkName("")
                        .timestamp(Instant.now())
                        .result(BigDecimal.valueOf(10))
                        .executionTime(10L)
                        .build()
                )
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_EMPTY));
    }

    @Test
    void getCheckHistoryDto_whenTimestampIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .checkName("check-name")
                        .result(BigDecimal.valueOf(10))
                        .executionTime(10L)
                        .build()
                )
        );

        assertTrue(exception.getMessage().contains(TIMESTAMP_NULL));
    }

    @Test
    void getCheckHistoryDto_whenResultIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .checkName("check-name")
                        .timestamp(Instant.now())
                        .executionTime(10L)
                        .build()
                )
        );

        assertTrue(exception.getMessage().contains(RESULT_NULL));
    }

    @Test
    void getCheckHistoryDto_whenExecutionTimeIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .checkName("check-name")
                        .timestamp(Instant.now())
                        .result(BigDecimal.valueOf(10))
                        .build()
                )
        );

        assertTrue(exception.getMessage().contains(EXECUTION_TIME_NULL));
    }

    @Test
    void getCheckHistoryDto_whenAllProvided_thenReturnCheckHistoryDto() {
        String checkName = "check-name";
        Instant timestamp = Instant.now();
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 10;
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName(checkName)
                .timestamp(timestamp)
                .result(result)
                .executionTime(executionTime)
                .build();

        CheckHistoryDto checkHistoryDto = CheckHistoryDtoFactory.getCheckHistoryDto(checkHistory);

        assertNotNull(checkHistoryDto);
        assertEquals(checkName, checkHistoryDto.getCheckName());
        assertEquals(timestamp, checkHistoryDto.getTimestamp());
        assertEquals(0, result.compareTo(checkHistoryDto.getResult()));
        assertEquals(executionTime, checkHistoryDto.getExecutionTime());
    }

}