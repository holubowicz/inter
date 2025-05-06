package com.example.backend.check.model;

import com.example.backend.check.model.dto.CheckExecutionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckTrend {

    private Double trendPercentage;
    private CheckExecutionDTO lastCheck;

}
