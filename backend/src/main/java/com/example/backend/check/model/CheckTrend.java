package com.example.backend.check.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CheckTrend {

    private final BigDecimal lastResult;
    private final Double trendPercentage;

}
