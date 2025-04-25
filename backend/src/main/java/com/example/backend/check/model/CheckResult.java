package com.example.backend.check.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CheckResult {

    private final String name;
    private final String error;
    private final BigDecimal result;
    private final BigDecimal lastResult;
    private final Double trendPercentage;

}
