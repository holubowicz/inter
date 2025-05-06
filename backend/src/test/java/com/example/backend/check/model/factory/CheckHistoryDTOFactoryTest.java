package com.example.backend.check.model.factory;

import com.example.backend.check.common.exception.CheckHistoryNullException;
import com.example.backend.check.common.exception.ExecutionTimeNullException;
import com.example.backend.check.common.exception.ResultNullException;
import com.example.backend.check.common.exception.TimestampNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.CheckHistoryDTO;
import com.example.backend.database.schema.CheckHistory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static com.example.backend.check.model.factory.CheckHistoryDTOFactory.getCheckHistoryDTO;
import static org.junit.jupiter.api.Assertions.*;

class CheckHistoryDTOFactoryTest {

    @Test
    void getCheckHistoryDTO_whenCheckHistoryIsNull_thenThrowsCheckHistoryNullException() {
        assertThrows(CheckHistoryNullException.class, () -> getCheckHistoryDTO(null));
    }

    @Test
    void getCheckHistoryDTO_whenCheckNameIsNull_thenThrowsNameNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameNullException.class, () -> getCheckHistoryDTO(checkHistory));
    }

    @Test
    void getCheckHistoryDTO_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameEmptyException.class, () -> getCheckHistoryDTO(checkHistory));
    }

    @Test
    void getCheckHistoryDTO_whenTimestampIsNull_thenThrowsTimestampNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(TimestampNullException.class, () -> getCheckHistoryDTO(checkHistory));
    }

    @Test
    void getCheckHistoryDTO_whenResultIsNull_thenThrowsResultNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .executionTime(10L)
                .build();

        assertThrows(ResultNullException.class, () -> getCheckHistoryDTO(checkHistory));
    }

    @Test
    void getCheckHistoryDTO_whenExecutionTimeIsNull_thenThrowsExecutionTimeNullException() {
        CheckHistory checkHistory = CheckHistory.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .build();

        assertThrows(ExecutionTimeNullException.class, () -> getCheckHistoryDTO(checkHistory));
    }

    @Test
    void getCheckHistoryDTO_whenAllProvided_thenReturnsCheckHistoryDTO() {
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

        CheckHistoryDTO checkHistoryDTO = getCheckHistoryDTO(checkHistory);

        assertNotNull(checkHistoryDTO);
        assertEquals(checkName, checkHistoryDTO.getCheckName());
        assertEquals(timestamp, checkHistoryDTO.getTimestamp());
        assertEquals(0, result.compareTo(checkHistoryDTO.getResult()));
        assertEquals(executionTime, checkHistoryDTO.getExecutionTime());
    }

}