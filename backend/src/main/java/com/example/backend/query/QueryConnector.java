package com.example.backend.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class QueryConnector {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public QueryConnector(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> runQuery(Query query) {
        if (query.getQuery().isEmpty()) return new ArrayList<>();
        return jdbcTemplate.queryForList(query.getQuery());
    }

}
