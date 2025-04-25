package com.example.backend.check.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Check {

    private final String name;
    private final String query;
    private final String error;

}
