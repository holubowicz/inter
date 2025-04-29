package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckDto;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.Instant;

import static com.example.backend.check.CheckErrorMessages.*;

@UtilityClass
public final class CheckDtoFactory {

    public static CheckDto createCheckDto(String checkName, BigDecimal lastResult, Instant lastTimestamp, Long lastExecutionTime) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY);
        }

        if (lastResult == null) {
            throw new IllegalArgumentException(LAST_RESULT_NULL);
        }

        if (lastTimestamp == null) {
            throw new IllegalArgumentException(LAST_TIMESTAMP_NULL);
        }

        if (lastExecutionTime == null) {
            throw new IllegalArgumentException(LAST_EXECUTION_TIME_NULL);
        }

        return CheckDto.builder()
                .name(checkName)
                .lastResult(lastResult)
                .lastTimestamp(lastTimestamp)
                .lastExecutionTime(lastExecutionTime)
                .build();
    }

    public static CheckDto createNameCheckDto(String checkName) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY);
        }

        return CheckDto.builder().name(checkName).build();
    }

}
