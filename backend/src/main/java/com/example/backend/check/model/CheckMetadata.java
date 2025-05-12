package com.example.backend.check.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CheckMetadata {

    private final String name;
    private final String category;

}
