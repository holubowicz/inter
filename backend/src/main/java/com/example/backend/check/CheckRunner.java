package com.example.backend.check;

import com.example.backend.check.common.exception.db.DatabaseBadSqlException;
import com.example.backend.check.common.exception.db.DatabaseQueryTimeoutException;
import com.example.backend.check.common.exception.db.InternalDatabaseException;
import com.example.backend.check.common.exception.db.TestedDatabaseException;
import com.example.backend.check.common.validator.NameValidator;
import com.example.backend.check.model.*;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import com.example.backend.check.model.dto.factory.CheckExecutionDTOFactory;
import com.example.backend.check.model.repository.CheckExecutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static com.example.backend.check.model.dto.factory.CheckExecutionDTOFactory.createCheckExecutionDTO;
import static com.example.backend.check.model.factory.CheckResultFactory.createNameErrorCheckResult;

@Slf4j
@Component
public class CheckRunner {

    @Value("${app.db.query-timeout-seconds}")
    private int QUERY_TIMEOUT_SECONDS;

    private final JdbcTemplate jdbcTemplate;
    private final CheckExecutionRepository checkExecutionRepository;

    @Autowired
    public CheckRunner(@Qualifier("testedJdbcTemplate") JdbcTemplate jdbcTemplate,
                       CheckExecutionRepository checkExecutionRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.checkExecutionRepository = checkExecutionRepository;
    }

    public CheckDTO getCheckDTO(CheckMetadata metadata) {
        NameValidator.validate(metadata.getName());
        return checkExecutionRepository
                .findTopByCheckNameOrderByTimestampDesc(metadata.getName())
                .map(CheckExecutionDTOFactory::createCheckExecutionDTO)
                .map(checkExecutionDTO -> new CheckDTO(
                        metadata,
                        checkExecutionDTO
                ))
                .orElseGet(() -> CheckDTO.builder().metadata(metadata).build());
    }

    public List<CheckExecution> getCheckExecutions(String checkName) {
        NameValidator.validate(checkName);
        return checkExecutionRepository.findByCheckNameOrderByTimestamp(checkName.toLowerCase());
    }

    public CheckResult runCheck(Check check) {
        try {
            return runCheckInternal(check);
        } catch (RuntimeException e) {
            log.warn(e.getMessage(), e.getCause());
            return createNameErrorCheckResult(check.getMetadata(), e.getMessage());
        }
    }

    private CheckResult runCheckInternal(Check check) {
        if (check.getError() != null) {
            return createNameErrorCheckResult(check.getMetadata(), check.getError());
        }

        long startTime = System.currentTimeMillis();
        BigDecimal currentResult = getQueryResult(check.getQuery());
        long executionTime = System.currentTimeMillis() - startTime;

        CheckTrend checkTrend = calculateTrend(check.getMetadata().getName(), currentResult);
        CheckExecution insertCheckExecution = CheckExecution.builder()
                .checkName(check.getMetadata().getName())
                .result(currentResult)
                .executionTime(executionTime)
                .build();
        CheckExecutionDTO currentCheckExecutionDTO = saveCheckToHistory(insertCheckExecution);

        return CheckResult.builder()
                .metadata(check.getMetadata())
                .trendPercentage(checkTrend.getTrendPercentage())
                .check(currentCheckExecutionDTO)
                .lastCheck(checkTrend.getLastCheck())
                .build();
    }

    public BigDecimal getQueryResult(String query) {
        try {
            return jdbcTemplate.query(
                    con -> {
                        PreparedStatement ps = con.prepareStatement(query);
                        ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
                        return ps;
                    },
                    rs -> {
                        if (rs.next()) {
                            return rs.getBigDecimal(1);
                        }
                        return null;
                    }
            );
        } catch (DataAccessResourceFailureException e) {
            throw new DatabaseQueryTimeoutException(e);
        } catch (BadSqlGrammarException e) {
            throw new DatabaseBadSqlException(e);
        } catch (Exception e) {
            throw new TestedDatabaseException(e);
        }
    }

    public CheckExecutionDTO saveCheckToHistory(CheckExecution insertCheckExecution) {
        try {
            CheckExecution checkExecution = checkExecutionRepository.save(insertCheckExecution);
            return createCheckExecutionDTO(checkExecution);
        } catch (Exception e) {
            throw new InternalDatabaseException(e);
        }
    }

    public CheckTrend calculateTrend(String checkName, BigDecimal currentResult) {
        Optional<CheckExecution> lastCheckExecutionOpt;
        try {
            lastCheckExecutionOpt = checkExecutionRepository.findTopByCheckNameOrderByTimestampDesc(checkName);
        } catch (Exception e) {
            throw new InternalDatabaseException(e);
        }

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
