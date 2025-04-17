package com.example.backend.query;

import lombok.Data;

@Data
public class QueryDto {

    private final String name;

    public static QueryDto from(Query query) {
        return new QueryDto(query.getName());
    }

}
