package com.example.backend.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CheckConnector {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CheckConnector(@Qualifier("testedJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CheckResult runCheck(Check check) {
        String name = check.getName();
        String error = check.getError();

        if (error != null) {
            return CheckResult
                    .builder()
                    .name(name)
                    .error(error)
                    .build();
        }

        try {
            BigDecimal result = jdbcTemplate.queryForObject(check.getQuery(), BigDecimal.class);
            return CheckResult
                    .builder()
                    .name(name)
                    .result(result)
                    .build();
        } catch (Exception e) {
            return CheckResult
                    .builder()
                    .name(name)
                    .error("Failed to query db")
                    .build();
        }
    }

}
