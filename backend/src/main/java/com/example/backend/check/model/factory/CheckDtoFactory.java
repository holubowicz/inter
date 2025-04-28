package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckDto;

import java.math.BigDecimal;
import java.time.Instant;

public class CheckDtoFactory {

    public static final String CHECK_NAME_NULL_ERROR = "Check name is null";
    public static final String CHECK_NAME_EMPTY_ERROR = "Check name is empty";
    public static final String LAST_RESULT_NULL_ERROR = "Last result is null";
    public static final String LAST_TIMESTAMP_NULL_ERROR = "Last timestamp is null";

    public static CheckDto createCheckDto(String checkName, BigDecimal lastResult, Instant lastTimestamp) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL_ERROR);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY_ERROR);
        }

        if (lastResult == null) {
            throw new IllegalArgumentException(LAST_RESULT_NULL_ERROR);
        }

        if (lastTimestamp == null) {
            throw new IllegalArgumentException(LAST_TIMESTAMP_NULL_ERROR);
        }

        return CheckDto.builder()
                .name(checkName)
                .lastResult(lastResult)
                .lastTimestamp(lastTimestamp)
                .build();
    }

    public static CheckDto createNameCheckDto(String checkName) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL_ERROR);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY_ERROR);
        }

        return CheckDto.builder().name(checkName).build();
    }

}
