package com.example.backend.check.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class CheckDto {

    private final String name;
    private final BigDecimal lastResult;
    private final Instant lastTimestamp;
    private final Long lastExecutionTime;

}
