package com.example.backend.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class QueryConnector {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public QueryConnector(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public QueryResult runQuery(Query query) {
        if (query.getQuery().isEmpty()) {
            return QueryResult
                    .builder()
                    .queryName(query.getName())
                    .build();
        }

        BigDecimal result = jdbcTemplate.queryForObject(query.getQuery(), BigDecimal.class);
        return QueryResult
                .builder()
                .queryName(query.getName())
                .result(result)
                .build();
    }

}
