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

import static org.junit.jupiter.api.Assertions.*;

class CheckHistoryDtoFactoryTest {

    @Test
    void getCheckHistoryDto_whenCheckHistoryIsNull_thenThrowsCheckHistoryNullException() {
        assertThrows(CheckHistoryNullException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(null)
        );
    }

    @Test
    void getCheckHistoryDto_whenCheckNameIsNull_thenThrowsNameNullException() {
        assertThrows(NameNullException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .timestamp(Instant.now())
                        .result(BigDecimal.valueOf(10))
                        .executionTime(10L)
                        .build()
                )
        );
    }

    @Test
    void getCheckHistoryDto_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        assertThrows(NameEmptyException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .checkName("")
                        .timestamp(Instant.now())
                        .result(BigDecimal.valueOf(10))
                        .executionTime(10L)
                        .build()
                )
        );
    }

    @Test
    void getCheckHistoryDto_whenTimestampIsNull_thenThrowsTimestampNullException() {
        assertThrows(TimestampNullException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .checkName("check-name")
                        .result(BigDecimal.valueOf(10))
                        .executionTime(10L)
                        .build()
                )
        );
    }

    @Test
    void getCheckHistoryDto_whenResultIsNull_thenThrowsResultNullException() {
        assertThrows(ResultNullException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .checkName("check-name")
                        .timestamp(Instant.now())
                        .executionTime(10L)
                        .build()
                )
        );
    }

    @Test
    void getCheckHistoryDto_whenExecutionTimeIsNull_thenThrowsExecutionTimeNullException() {
        assertThrows(ExecutionTimeNullException.class, () ->
                CheckHistoryDtoFactory.getCheckHistoryDto(CheckHistory.builder()
                        .checkName("check-name")
                        .timestamp(Instant.now())
                        .result(BigDecimal.valueOf(10))
                        .build()
                )
        );
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

        CheckHistoryDto checkHistoryDto = CheckHistoryDtoFactory.getCheckHistoryDto(checkHistory);

        assertNotNull(checkHistoryDto);
        assertEquals(checkName, checkHistoryDto.getCheckName());
        assertEquals(timestamp, checkHistoryDto.getTimestamp());
        assertEquals(0, result.compareTo(checkHistoryDto.getResult()));
        assertEquals(executionTime, checkHistoryDto.getExecutionTime());
    }

}