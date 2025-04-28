package com.example.backend.check.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

// TODO: add last execution time

@Getter
@Builder
public class CheckDto {

    private final String name;
    private final BigDecimal lastResult;
    private final Instant lastTimestamp;
//    private final Long lastExecutionTime;

}
