package com.example.backend.check.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class CheckHistoryDTO {

    private String checkName;
    private Instant timestamp;
    private BigDecimal result;
    private Long executionTime;

}
