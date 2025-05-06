package com.example.backend.check.model.factory.dto.factory;

import com.example.backend.check.common.exception.CheckExecutionNullException;
import com.example.backend.check.common.exception.ExecutionTimeNullException;
import com.example.backend.check.common.exception.ResultNullException;
import com.example.backend.check.common.exception.TimestampNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.CheckExecution;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static com.example.backend.check.model.dto.factory.CheckExecutionDTOFactory.createCheckExecutionDTO;
import static org.junit.jupiter.api.Assertions.*;

class CheckExecutionDTOFactoryTest {

    @Test
    void createCheckExecutionDTO_whenCheckExecutionIsNull_thenThrowsCheckExecutionNullException() {
        assertThrows(CheckExecutionNullException.class, () -> createCheckExecutionDTO(null));
    }

    @Test
    void createCheckExecutionDTO_whenCheckNameIsNull_thenThrowsNameNullException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameNullException.class, () -> createCheckExecutionDTO(checkExecution));
    }

    @Test
    void createCheckExecutionDTO_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName("")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(NameEmptyException.class, () -> createCheckExecutionDTO(checkExecution));
    }

    @Test
    void createCheckExecutionDTO_whenTimestampIsNull_thenThrowsTimestampNullException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName("check-name")
                .result(BigDecimal.valueOf(10))
                .executionTime(10L)
                .build();

        assertThrows(TimestampNullException.class, () -> createCheckExecutionDTO(checkExecution));
    }

    @Test
    void createCheckExecutionDTO_whenResultIsNull_thenThrowsResultNullException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .executionTime(10L)
                .build();

        assertThrows(ResultNullException.class, () -> createCheckExecutionDTO(checkExecution));
    }

    @Test
    void createCheckExecutionDTO_whenExecutionTimeIsNull_thenThrowsExecutionTimeNullException() {
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName("check-name")
                .timestamp(Instant.now())
                .result(BigDecimal.valueOf(10))
                .build();

        assertThrows(ExecutionTimeNullException.class, () -> createCheckExecutionDTO(checkExecution));
    }

    @Test
    void createCheckExecutionDTO_whenAllProvided_thenReturnsCheckExecutionDTO() {
        String checkName = "check-name";
        Instant timestamp = Instant.now();
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 10;
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName(checkName)
                .timestamp(timestamp)
                .result(result)
                .executionTime(executionTime)
                .build();

        CheckExecutionDTO checkExecutionDTO = createCheckExecutionDTO(checkExecution);

        assertNotNull(checkExecutionDTO);
        assertEquals(checkName, checkExecutionDTO.getCheckName());
        assertEquals(timestamp, checkExecutionDTO.getTimestamp());
        assertEquals(0, result.compareTo(checkExecutionDTO.getResult()));
        assertEquals(executionTime, checkExecutionDTO.getExecutionTime());
    }

}