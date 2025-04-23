package com.example.backend.check;

import com.example.backend.database.internal.ResultHistory;
import com.example.backend.database.internal.ResultHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CheckConnector {

    private final JdbcTemplate jdbcTemplate;
    private final ResultHistoryRepository resultHistoryRepository;

    @Autowired
    public CheckConnector(@Qualifier("testedJdbcTemplate") JdbcTemplate jdbcTemplate, ResultHistoryRepository resultHistoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.resultHistoryRepository = resultHistoryRepository;
    }

    public CheckResult runCheck(Check check) {
        CheckResult.CheckResultBuilder checkResultBuilder = CheckResult.builder()
                .name(check.getName());

        if (check.getError() != null) {
            return checkResultBuilder.error(check.getError()).build();
        }

        try {
            BigDecimal currentResult = jdbcTemplate.queryForObject(check.getQuery(), BigDecimal.class);

            calculateTrend(check.getName(), currentResult, checkResultBuilder);
            saveResultToHistory(check.getName(), currentResult);

            return checkResultBuilder.result(currentResult).build();
        } catch (DataAccessException e) {
            return checkResultBuilder.error("Failed to query database").build();
        }
    }

    private void calculateTrend(String checkName, BigDecimal currentResult, CheckResult.CheckResultBuilder builder) {
        Optional<ResultHistory> lastResultOpt = resultHistoryRepository.findTopByCheckNameOrderByTimestampDesc(checkName);
        if (lastResultOpt.isEmpty()) {
            return;
        }

        BigDecimal lastResult = lastResultOpt.get().getResult();
        builder.result(lastResult);

        double previous = lastResult.doubleValue();
        double current = currentResult.doubleValue();

        if (previous != 0) {
            double trend = (current / previous - 1) * 100;
            double roundedTrend = Math.round(trend * 100) / 100.0;
            builder.trendPercentage(roundedTrend);
        }
    }

    private void saveResultToHistory(String checkName, BigDecimal result) {
        ResultHistory resultHistory = ResultHistory.builder()
                .checkName(checkName)
                .result(result)
                .build();

        resultHistoryRepository.save(resultHistory);
    }

}
