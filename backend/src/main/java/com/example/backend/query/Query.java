package com.example.backend.query;

import lombok.Data;

@Data
public class Query {

    private final String name;
    private final String query;
    private final boolean isAvailable;

}
