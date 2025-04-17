package com.example.backend.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Query {

    private final String name;
    private final String query;

}
