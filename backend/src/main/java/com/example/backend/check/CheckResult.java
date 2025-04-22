package com.example.backend.check;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CheckResult {

    private final String name;
    private final BigDecimal result;
    private final String error;

}
