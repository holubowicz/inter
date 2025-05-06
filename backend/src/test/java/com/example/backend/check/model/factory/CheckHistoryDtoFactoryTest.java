package com.example.backend.check.model.factory;

import com.example.backend.check.common.exception.CheckHistoryNullException;
import com.example.backend.check.common.exception.ExecutionTimeNullException;
import com.example.backend.check.common.exception.ResultNullException;
import com.example.backend.check.common.exception.TimestampNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.CheckHistoryDto;
import com.example.backend.database.schema.CheckHistory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static com.example.backend.check.model.factory.CheckHistoryDtoFactory.getCheckHistoryDto;
import static org.junit.jupiter.api.Assertions.*;

class CheckHistoryDtoFactoryTest {

    @Test
    void getCheckHistoryDto_whenCheckHistoryIsNull_thenThrowsCheckHistoryNullException() {
        assertThrows(CheckHistoryNullException.class, () -> getCheckHistoryDto(null));
    }

    @Test
    void getCheckHistoryDto_whenCheckNameIsNull_thenThrowsNameNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameNullException.class, () -> getCheckHistoryDto(checkHistory));
    }

    @Test
    void getCheckHistoryDto_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameEmptyException.class, () -> getCheckHistoryDto(checkHistory));
    }

    @Test
    void getCheckHistoryDto_whenTimestampIsNull_thenThrowsTimestampNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(TimestampNullException.class, () -> getCheckHistoryDto(checkHistory));
    }

    @Test
    void getCheckHistoryDto_whenResultIsNull_thenThrowsResultNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .executionTime(10L)
                .build();

        assertThrows(ResultNullException.class, () -> getCheckHistoryDto(checkHistory));
    }

    @Test
    void getCheckHistoryDto_whenExecutionTimeIsNull_thenThrowsExecutionTimeNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .build();

        assertThrows(ExecutionTimeNullException.class, () -> getCheckHistoryDto(checkHistory));
    }

    @Test
    void getCheckHistoryDto_whenAllProvided_thenReturnsCheckHistoryDto() {
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

        CheckHistoryDto checkHistoryDto = getCheckHistoryDto(checkHistory);

        assertNotNull(checkHistoryDto);
        assertEquals(checkName, checkHistoryDto.getCheckName());
        assertEquals(timestamp, checkHistoryDto.getTimestamp());
        assertEquals(0, result.compareTo(checkHistoryDto.getResult()));
        assertEquals(executionTime, checkHistoryDto.getExecutionTime());
    }

}