package com.example.backend.check;

import com.example.backend.check.common.exception.CheckNullException;
import com.example.backend.check.common.validator.ExecutionTimeValidator;
import com.example.backend.check.common.validator.NameValidator;
import com.example.backend.check.common.validator.ResultValidator;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.CheckTrend;
import com.example.backend.check.model.factory.CheckDtoFactory;
import com.example.backend.database.schema.CheckHistory;
import com.example.backend.database.schema.CheckHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.backend.check.common.ErrorMessages.FAILED_QUERY_DB;

@Slf4j
@Component
public class CheckRunner {

    private final JdbcTemplate jdbcTemplate;
    private final CheckHistoryRepository checkHistoryRepository;

    @Autowired
    public CheckRunner(@Qualifier("testedJdbcTemplate") JdbcTemplate jdbcTemplate,
                       CheckHistoryRepository checkHistoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.checkHistoryRepository = checkHistoryRepository;
    }

    public CheckDto getCheckDto(String checkName) {
        NameValidator.validate(checkName);
        return checkHistoryRepository
                .findTopByCheckNameOrderByTimestampDesc(checkName)
                .map(checkHistory -> new CheckDto(
                        checkName,
                        checkHistory.getResult(),
                        checkHistory.getTimestamp(),
                        checkHistory.getExecutionTime()
                ))
                .orElseGet(() -> CheckDtoFactory.createNameCheckDto(checkName));
    }

    public List<CheckHistory> getCheckHistoryList(String checkName) {
        NameValidator.validate(checkName);
        return checkHistoryRepository.findByCheckNameOrderByTimestamp(checkName.toLowerCase());
    }

    // TODO: make it shorter
    public CheckResult runCheck(Check check) {
        if (check == null) {
            throw new CheckNullException();
        }

        NameValidator.validate(check.getName());

        CheckResult.CheckResultBuilder builder = CheckResult.builder()
                .name(check.getName());

        if (check.getError() != null) {
            return builder.error(check.getError()).build();
        }

        try {
            long startTime = System.currentTimeMillis();

            BigDecimal currentResult = jdbcTemplate.queryForObject(check.getQuery(), BigDecimal.class);

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            CheckTrend checkTrend = calculateTrend(check.getName(), currentResult);
            builder.lastResult(checkTrend.getLastResult())
                    .lastTimestamp(checkTrend.getLastTimestamp())
                    .trendPercentage(checkTrend.getTrendPercentage());

            saveResultToHistory(check.getName(), currentResult, executionTime);

            return builder
                    .result(currentResult)
                    .executionTime(executionTime)
                    .build();
        } catch (Exception e) {
            log.error(FAILED_QUERY_DB);
            return builder.error(FAILED_QUERY_DB).build();
        }
    }

    public CheckTrend calculateTrend(String checkName, BigDecimal currentResult) {
        NameValidator.validate(checkName);
        ResultValidator.validate(currentResult);

        Optional<CheckHistory> lastResultOpt = checkHistoryRepository.findTopByCheckNameOrderByTimestampDesc(checkName);
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

    public void saveResultToHistory(String checkName, BigDecimal result, Long executionTime) {
        NameValidator.validate(checkName);
        ResultValidator.validate(result);
        ExecutionTimeValidator.validate(executionTime);

        CheckHistory checkHistory = CheckHistory.builder()
                .checkName(checkName)
                .result(result)
                .executionTime(executionTime)
                .build();

        checkHistoryRepository.save(checkHistory);
    }

}
