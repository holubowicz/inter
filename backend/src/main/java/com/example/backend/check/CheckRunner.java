package com.example.backend.check;

import com.example.backend.check.common.exception.CheckNullException;
import com.example.backend.check.common.validator.ExecutionTimeValidator;
import com.example.backend.check.common.validator.NameValidator;
import com.example.backend.check.common.validator.ResultValidator;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckExecution;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.CheckTrend;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import com.example.backend.check.model.dto.factory.CheckExecutionDTOFactory;
import com.example.backend.check.model.repository.CheckExecutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.backend.check.common.error.message.DatabaseErrorMessage.FAILED_QUERY_DB;
import static com.example.backend.check.common.error.message.DatabaseErrorMessage.FAILED_QUERY_INTERNAL_DB;
import static com.example.backend.check.model.dto.factory.CheckDTOFactory.createNameCheckDTO;
import static com.example.backend.check.model.dto.factory.CheckExecutionDTOFactory.createCheckExecutionDTO;
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
                .map(CheckExecutionDTOFactory::createCheckExecutionDTO)
                .map(checkExecutionDTO -> new CheckDTO(
                        checkName,
                        checkExecutionDTO
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

        BigDecimal currentResult;
        long startTime = System.currentTimeMillis();
        // TODO: separate to other method
        try {
            currentResult = jdbcTemplate.queryForObject(check.getQuery(), BigDecimal.class);
        } catch (Exception e) {
            return createNameErrorCheckResult(check.getName(), FAILED_QUERY_DB);
        }
        long executionTime = System.currentTimeMillis() - startTime;

        CheckTrend checkTrend;
        CheckExecution currentCheckExecution;
        try {
            // TODO: separate to other method
            checkTrend = calculateTrend(check.getName(), currentResult);
            // TODO: separate to other method
            currentCheckExecution = saveResultToHistory(check.getName(), currentResult, executionTime);
        } catch (Exception e) {
            return createNameErrorCheckResult(check.getName(), FAILED_QUERY_INTERNAL_DB);
        }

        CheckExecutionDTO currentCheckExecutionDTO = createCheckExecutionDTO(currentCheckExecution);

        // TODO: create method to create CheckResult using Factory
        return CheckResult.builder()
                .name(check.getName())
                .trendPercentage(checkTrend.getTrendPercentage())
                .check(currentCheckExecutionDTO)
                .lastCheck(checkTrend.getLastCheck())
                .build();
    }

    public CheckTrend calculateTrend(String checkName, BigDecimal currentResult) {
        NameValidator.validate(checkName);
        ResultValidator.validate(currentResult);

        Optional<CheckExecution> lastCheckExecutionOpt = checkExecutionRepository
                .findTopByCheckNameOrderByTimestampDesc(checkName);

        if (lastCheckExecutionOpt.isEmpty()) {
            return new CheckTrend();
        }

        CheckExecutionDTO lastCheckExecutionDTO = createCheckExecutionDTO(lastCheckExecutionOpt.get());

        double current = currentResult.doubleValue();
        double previous = lastCheckExecutionDTO.getResult().doubleValue();

        Double trendPercentage = null;
        if (previous != 0) {
            double trend = (current / previous - 1) * 100;
            trendPercentage = Math.round(trend * 100) / 100.0;
        }

        return new CheckTrend(trendPercentage, lastCheckExecutionDTO);
    }

    public CheckExecution saveResultToHistory(String checkName, BigDecimal result, Long executionTime) {
        NameValidator.validate(checkName);
        ResultValidator.validate(result);
        ExecutionTimeValidator.validate(executionTime);

        // TODO: create factory for CheckExecution (INSERT)
        CheckExecution checkExecution = CheckExecution.builder()
                .checkName(checkName)
                .result(result)
                .executionTime(executionTime)
                .build();

        return checkExecutionRepository.save(checkExecution);
    }

}
