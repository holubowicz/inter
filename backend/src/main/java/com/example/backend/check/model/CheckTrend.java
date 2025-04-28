package com.example.backend.check.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
public class CheckTrend {

    private final BigDecimal lastResult;
    private final Instant lastTimestamp;
    private final Double trendPercentage;

}
