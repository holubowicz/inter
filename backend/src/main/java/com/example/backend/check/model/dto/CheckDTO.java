package com.example.backend.check.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CheckDTO {

    private final String name;
    private final CheckExecutionDTO lastCheck;

}
