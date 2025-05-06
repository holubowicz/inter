package com.example.backend.check.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class CheckExecutionDTO {

    private BigDecimal result;
    private Long executionTime;
    private Instant timestamp;

}
