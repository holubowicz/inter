package com.example.backend.check.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

// TODO: add execution time

@Getter
@Builder
public class CheckResult {

    private final String name;
    private final String error;
    private final BigDecimal result;
    //    private final Long executionTime;
    private final BigDecimal lastResult;
    private final Instant lastTimestamp;
    private final Double trendPercentage;

}
