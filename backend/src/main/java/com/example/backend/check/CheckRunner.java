package com.example.backend.check;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.CheckTrend;
import com.example.backend.database.schema.ResultHistory;
import com.example.backend.database.schema.ResultHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
public class CheckRunner {

    public static String CHECK_NULL_ERROR = "Check is null";
    public static String CHECK_NAME_NULL_ERROR = "Check name is null";
    public static String CHECK_NAME_EMPTY_ERROR = "Check name is empty";
    public static String RESULT_NULL_ERROR = "Result is null";
    public static String FAILED_QUERY_DB_ERROR = "Failed to query database";

    private final JdbcTemplate jdbcTemplate;
    private final ResultHistoryRepository resultHistoryRepository;

    @Autowired
    public CheckRunner(@Qualifier("testedJdbcTemplate") JdbcTemplate jdbcTemplate,
                       ResultHistoryRepository resultHistoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.resultHistoryRepository = resultHistoryRepository;
    }

    public CheckDto getCheckDto(String checkName) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL_ERROR);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY_ERROR);
        }

        Optional<ResultHistory> resultHistoryOpt = resultHistoryRepository
                .findTopByCheckNameOrderByTimestampDesc(checkName);

        if (resultHistoryOpt.isEmpty()) {
            return CheckDto.builder()
                    .name(checkName)
                    .build();
        }

        ResultHistory resultHistory = resultHistoryOpt.get();

        return CheckDto.builder()
                .name(checkName)
                .lastResult(resultHistory.getResult())
                .lastTimestamp(resultHistory.getTimestamp())
                .build();
    }

    // TODO: only allow to run SELECT queries
    public CheckResult runCheck(Check check) {
        if (check == null) {
            throw new IllegalArgumentException(CHECK_NULL_ERROR);
        }

        if (check.getName() == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL_ERROR);
        }
        if (check.getName().isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY_ERROR);
        }

        CheckResult.CheckResultBuilder builder = CheckResult.builder()
                .name(check.getName());

        if (check.getError() != null) {
            return builder.error(check.getError()).build();
        }

        try {
            BigDecimal currentResult = jdbcTemplate.queryForObject(check.getQuery(), BigDecimal.class);

            CheckTrend checkTrend = calculateTrend(check.getName(), currentResult);
            builder.lastResult(checkTrend.getLastResult())
                    .lastTimestamp(checkTrend.getLastTimestamp())
                    .trendPercentage(checkTrend.getTrendPercentage());

            saveResultToHistory(check.getName(), currentResult);

            return builder.result(currentResult).build();
        } catch (Exception e) {
            log.error(FAILED_QUERY_DB_ERROR);
            return builder.error(FAILED_QUERY_DB_ERROR).build();
        }
    }

    public CheckTrend calculateTrend(String checkName, BigDecimal currentResult) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL_ERROR);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY_ERROR);
        }

        if (currentResult == null) {
            throw new IllegalArgumentException(RESULT_NULL_ERROR);
        }

        Optional<ResultHistory> lastResultOpt = resultHistoryRepository.findTopByCheckNameOrderByTimestampDesc(checkName);
        if (lastResultOpt.isEmpty()) {
            return CheckTrend.builder().build();
        }

        CheckTrend.CheckTrendBuilder builder = CheckTrend.builder();

        BigDecimal lastResult = lastResultOpt.get().getResult();
        builder.lastResult(lastResult)
                .lastTimestamp(lastResultOpt.get().getTimestamp());

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
