package com.example.backend.check;

import com.example.backend.check.common.exception.CheckNullException;
import com.example.backend.check.common.validator.ExecutionTimeValidator;
import com.example.backend.check.common.validator.NameValidator;
import com.example.backend.check.common.validator.ResultValidator;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.CheckTrend;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.database.schema.CheckExecution;
import com.example.backend.database.schema.CheckExecutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.backend.check.common.error.message.DatabaseErrorMessage.FAILED_QUERY_DB;
import static com.example.backend.check.common.error.message.DatabaseErrorMessage.FAILED_SAVE_TO_INTERNAL_DB;
import static com.example.backend.check.model.dto.factory.CheckDTOFactory.createNameCheckDTO;
import static com.example.backend.check.model.factory.CheckResultFactory.createNameErrorCheckResult;

@Slf4j
@Component
public class CheckRunner {

    private final JdbcTemplate jdbcTemplate;
    private final CheckExecutionRepository checkExecutionRepository;

    @Autowired
    public CheckRunner(@Qualifier("testedJdbcTemplate") JdbcTemplate jdbcTemplate,
                       CheckExecutionRepository checkExecutionRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.checkExecutionRepository = checkExecutionRepository;
    }

    public CheckDTO getCheckDTO(String checkName) {
        NameValidator.validate(checkName);
        return checkExecutionRepository
                .findTopByCheckNameOrderByTimestampDesc(checkName)
                .map(checkExecution -> new CheckDTO(
                        checkName,
                        checkExecution.getResult(),
                        checkExecution.getTimestamp(),
                        checkExecution.getExecutionTime()
                ))
                .orElseGet(() -> createNameCheckDTO(checkName));
    }

    public List<CheckExecution> getCheckExecutions(String checkName) {
        NameValidator.validate(checkName);
        return checkExecutionRepository.findByCheckNameOrderByTimestamp(checkName.toLowerCase());
    }

    public CheckResult runCheck(Check check) {
        if (check == null) {
            throw new CheckNullException();
        }

        NameValidator.validate(check.getName());

        if (check.getError() != null) {
            return createNameErrorCheckResult(check.getName(), check.getError());
        }

        try {
            long startTime = System.currentTimeMillis();
            // TODO: secure ran queries, allow only to run SELECT queries
            BigDecimal currentResult = jdbcTemplate.queryForObject(check.getQuery(), BigDecimal.class);
            long executionTime = System.currentTimeMillis() - startTime;

            CheckTrend checkTrend = calculateTrend(check.getName(), currentResult);
            saveResultToHistory(check.getName(), currentResult, executionTime);

            return new CheckResult(
                    check.getName(),
                    null,
                    currentResult,
                    executionTime,
                    checkTrend.getLastResult(),
                    checkTrend.getLastTimestamp(),
                    checkTrend.getTrendPercentage()
            );
        } catch (DataAccessException | IllegalArgumentException e) {
            log.error(FAILED_QUERY_DB, e);
            return createNameErrorCheckResult(check.getName(), FAILED_QUERY_DB);
        } catch (Exception e) {
            log.error(FAILED_SAVE_TO_INTERNAL_DB, e);
            return createNameErrorCheckResult(check.getName(), FAILED_SAVE_TO_INTERNAL_DB);
        }
    }

    public CheckTrend calculateTrend(String checkName, BigDecimal currentResult) {
        NameValidator.validate(checkName);
        ResultValidator.validate(currentResult);

        Optional<CheckExecution> lastResultOpt = checkExecutionRepository.findTopByCheckNameOrderByTimestampDesc(checkName);
        if (lastResultOpt.isEmpty()) {
            return CheckTrend.builder().build();
        }

        BigDecimal lastResult = lastResultOpt.get().getResult();
        CheckTrend.CheckTrendBuilder builder = CheckTrend.builder()
                .lastResult(lastResult)
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

        CheckExecution checkExecution = CheckExecution.builder()
                .checkName(checkName)
                .result(result)
                .executionTime(executionTime)
                .build();

        checkExecutionRepository.save(checkExecution);
    }

}
