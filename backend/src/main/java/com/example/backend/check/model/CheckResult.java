package com.example.backend.check.model;

import com.example.backend.check.model.dto.CheckExecutionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CheckResult {

    private final String error;
    private final Double trendPercentage;
    private final CheckMetadata metadata;
    private final CheckExecutionDTO check;
    private final CheckExecutionDTO lastCheck;

}
