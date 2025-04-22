package com.example.backend.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CheckConnector {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CheckConnector(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CheckResult runCheck(Check check) {
        if (check.getQuery().isEmpty()) {
            return CheckResult
                    .builder()
                    .name(check.getName())
                    .build();
        }

        BigDecimal result = jdbcTemplate.queryForObject(check.getQuery(), BigDecimal.class);

        return CheckResult
                .builder()
                .name(check.getName())
                .result(result)
                .build();
    }

}
