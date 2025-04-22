package com.example.backend.query;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class QueryResult {

    private final String queryName;
    private final BigDecimal result;

}
