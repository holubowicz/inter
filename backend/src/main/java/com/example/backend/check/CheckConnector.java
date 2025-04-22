package com.example.backend.check;

import com.example.backend.database.internal.Result;
import com.example.backend.database.internal.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class CheckConnector {

    private final JdbcTemplate jdbcTemplate;
    private final ResultRepository resultRepo;

    @Autowired
    public CheckConnector(@Qualifier("testedJdbcTemplate") JdbcTemplate jdbcTemplate, ResultRepository resultRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.resultRepo = resultRepo;
    }

    public CheckResult runCheck(Check check) {
        String error = check.getError();
        CheckResult.CheckResultBuilder checkResultBuilder = CheckResult.builder()
                .name(check.getName());

        if (error != null) {
            return checkResultBuilder.error(error).build();
        }

        try {
            BigDecimal queryResult = jdbcTemplate.queryForObject(check.getQuery(), BigDecimal.class);

            // save result to history
            resultRepo.save(Result.builder()
                    .timestamp(Instant.now())
                    .checkName(check.getName())
                    .result(queryResult)
                    .build());

            return checkResultBuilder.result(queryResult).build();
        } catch (DataAccessException e) {
            return checkResultBuilder.error("Failed to query database").build();
        }
    }

}
