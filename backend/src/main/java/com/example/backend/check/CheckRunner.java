package com.example.backend.check;

import com.example.backend.check.common.validator.NameValidator;
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
import static com.example.backend.check.model.factory.CheckExecutionFactory.createInsertCheckExecution;
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
        if (check.getError() != null) {
            return createNameErrorCheckResult(check.getName(), check.getError());
        }

        long startTime = System.currentTimeMillis();
        BigDecimal currentResult = getQueryResult(check.getQuery());
        long executionTime = System.currentTimeMillis() - startTime;

        if (currentResult == null) {
            log.warn(FAILED_QUERY_DB);
            return createNameErrorCheckResult(check.getName(), FAILED_QUERY_DB);
        }

        CheckTrend checkTrend = getCheckTrend(check.getName(), currentResult);
        CheckExecution insertCheckExecution = createInsertCheckExecution(check.getName(), currentResult, executionTime);
        CheckExecutionDTO currentCheckExecutionDTO = saveCheckToHistory(insertCheckExecution);

        if (checkTrend == null || currentCheckExecutionDTO == null) {
            log.warn(FAILED_QUERY_INTERNAL_DB);
            return createNameErrorCheckResult(check.getName(), FAILED_QUERY_INTERNAL_DB);
        }

        return CheckResult.builder()
                .name(check.getName())
                .trendPercentage(checkTrend.getTrendPercentage())
                .check(currentCheckExecutionDTO)
                .lastCheck(checkTrend.getLastCheck())
                .build();
    }

    public BigDecimal getQueryResult(String query) {
        try {
            return jdbcTemplate.queryForObject(query, BigDecimal.class);
        } catch (Exception e) {
            return null;
        }
    }

    public CheckTrend getCheckTrend(String checkName, BigDecimal currentResult) {
        try {
            return calculateTrend(checkName, currentResult);
        } catch (Exception e) {
            return null;
        }
    }

    public CheckExecutionDTO saveCheckToHistory(CheckExecution insertCheckExecution) {
        try {
            CheckExecution checkExecution = checkExecutionRepository.save(insertCheckExecution);
            return createCheckExecutionDTO(checkExecution);
        } catch (Exception e) {
            return null;
        }
    }

    public CheckTrend calculateTrend(String checkName, BigDecimal currentResult) {
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

}
