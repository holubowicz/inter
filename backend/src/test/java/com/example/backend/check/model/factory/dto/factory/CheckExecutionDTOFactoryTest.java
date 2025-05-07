package com.example.backend.check.model.factory.dto.factory;

import com.example.backend.check.model.CheckExecution;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static com.example.backend.check.model.dto.factory.CheckExecutionDTOFactory.createCheckExecutionDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckExecutionDTOFactoryTest {

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
        assertEquals(timestamp, checkExecutionDTO.getTimestamp());
        assertEquals(0, result.compareTo(checkExecutionDTO.getResult()));
        assertEquals(executionTime, checkExecutionDTO.getExecutionTime());
    }

}