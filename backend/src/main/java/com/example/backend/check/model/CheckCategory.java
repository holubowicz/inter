package com.example.backend.check.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckCategory {

    private final String name;
    private final long count;

}
