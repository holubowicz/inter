package com.example.backend.check;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.CheckTrend;
import com.example.backend.database.schema.ResultHistory;
import com.example.backend.database.schema.ResultHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CheckRunner {

    public static String CHECK_NAME_NULL_ERROR = "Check name is null";
    public static String CHECK_NAME_EMPTY_ERROR = "Check name is empty";
    public static String RESULT_NULL_ERROR = "Result is null";

    private final JdbcTemplate jdbcTemplate;
    private final ResultHistoryRepository resultHistoryRepository;

    @Autowired
    public CheckRunner(@Qualifier("testedJdbcTemplate") JdbcTemplate jdbcTemplate,
                       ResultHistoryRepository resultHistoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.resultHistoryRepository = resultHistoryRepository;
    }

    public CheckResult runCheck(Check check) {
        CheckResult.CheckResultBuilder builder = CheckResult.builder()
                .name(check.getName());

        if (check.getError() != null) {
            return builder.error(check.getError()).build();
        }

        try {
            BigDecimal currentResult = jdbcTemplate.queryForObject(check.getQuery(), BigDecimal.class);

            CheckTrend checkTrend = calculateTrend(check.getName(), currentResult);
            builder.lastResult(checkTrend.getLastResult())
                    .trendPercentage(checkTrend.getTrendPercentage());

            saveResultToHistory(check.getName(), currentResult);

            return builder.result(currentResult).build();
        } catch (Exception e) {
            return builder.error("Failed to query database").build();
        }
    }

    public CheckTrend calculateTrend(String checkName, BigDecimal currentResult) {
        Optional<ResultHistory> lastResultOpt = resultHistoryRepository.findTopByCheckNameOrderByTimestampDesc(checkName);
        if (lastResultOpt.isEmpty()) {
            return CheckTrend.builder().build();
        }

        CheckTrend.CheckTrendBuilder builder = CheckTrend.builder();

        BigDecimal lastResult = lastResultOpt.get().getResult();
        builder.lastResult(lastResult);

        double previous = lastResult.doubleValue();
        double current = currentResult.doubleValue();

        if (previous != 0) {
            double trend = (current / previous - 1) * 100;
            double roundedTrend = Math.round(trend * 100) / 100.0;
            builder.trendPercentage(roundedTrend);
        }

        return builder.build();
    }

    public void saveResultToHistory(String checkName, BigDecimal result) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL_ERROR);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY_ERROR);
        }

        if (result == null) {
            throw new IllegalArgumentException(RESULT_NULL_ERROR);
        }

        ResultHistory resultHistory = ResultHistory.builder()
                .checkName(checkName)
                .result(result)
                .build();

        resultHistoryRepository.save(resultHistory);
    }

}
