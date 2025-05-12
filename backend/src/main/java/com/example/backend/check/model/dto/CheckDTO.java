package com.example.backend.check.model.dto;

import com.example.backend.check.model.CheckMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CheckDTO {

    private final CheckMetadata metadata;
    private final CheckExecutionDTO lastCheck;

}
