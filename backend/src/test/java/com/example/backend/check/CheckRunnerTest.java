package com.example.backend.check;

import com.example.backend.check.common.exception.NameNullOrEmptyException;
import com.example.backend.check.common.exception.db.DatabaseBadSqlException;
import com.example.backend.check.common.exception.db.TestedDatabaseException;
import com.example.backend.check.model.*;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import com.example.backend.check.model.repository.CheckExecutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static com.example.backend.check.model.factory.CheckFactory.createCheck;
import static com.example.backend.check.model.factory.CheckFactory.createNameErrorCheck;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CheckRunnerTest {

    @Autowired
    private CheckRunner underTest;

    @Autowired
    private CheckExecutionRepository checkExecutionRepository;

    @Container
    private static final PostgreSQLContainer<?> testedPostgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withInitScript("schema/tested.sql");

    @Container
    private static final PostgreSQLContainer<?> internalPostgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void setTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.tested.url", testedPostgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.tested.username", testedPostgreSQLContainer::getUsername);
        registry.add("spring.datasource.tested.password", testedPostgreSQLContainer::getPassword);

        registry.add("spring.datasource.internal.url", internalPostgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.internal.username", internalPostgreSQLContainer::getUsername);
        registry.add("spring.datasource.internal.password", internalPostgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setupBeforeEach() {
        checkExecutionRepository.deleteAll();
    }


    @Test
    public void getCheckDTO_whenNoCheckExecutionSaved_thenReturnsNotFullCheckDTO() {
        CheckMetadata metadata = new CheckMetadata("check-name", "category");

        CheckDTO checkDTO = underTest.getCheckDTO(metadata);

        assertNotNull(checkDTO);
        assertEquals(metadata.getName(), checkDTO.getMetadata().getName());
        assertEquals(metadata.getCategory(), checkDTO.getMetadata().getCategory());
        assertNull(checkDTO.getLastCheck());
    }

    @Test
    public void getCheckDTO_whenCheckExecutionSaved_thenReturnsNotFullCheckDTO() {
        CheckMetadata metadata = new CheckMetadata("check-name", "category");
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Long lastExecutionTime = 10L;
        checkExecutionRepository.save(CheckExecution.builder()
                .checkName(metadata.getName())
                .result(lastResult)
                .executionTime(lastExecutionTime)
                .build());

        CheckDTO checkDTO = underTest.getCheckDTO(metadata);

        assertNotNull(checkDTO);
        assertEquals(metadata.getName(), checkDTO.getMetadata().getName());
        assertEquals(metadata.getCategory(), checkDTO.getMetadata().getCategory());
        assertNotNull(checkDTO.getLastCheck());
        assertEquals(0, lastResult.compareTo(checkDTO.getLastCheck().getResult()));
        assertNotNull(checkDTO.getLastCheck().getTimestamp());
        assertEquals(lastExecutionTime, checkDTO.getLastCheck().getExecutionTime());
    }


    @ParameterizedTest
    @NullAndEmptySource
    public void getCheckExecutions_whenCheckNameIsNullOrEmpty_thenThrowsNameNullOrEmptyException(String checkName) {
        assertThrows(NameNullOrEmptyException.class, () ->
                underTest.getCheckExecutions(checkName)
        );
    }

    @Test
    public void getCheckExecutions_whenCheckNameProvidedNoHistorySaved_thenReturnsEmptyCheckExecutions() {
        String checkName = "check-name";

        List<CheckExecution> checkExecutionList = underTest.getCheckExecutions(checkName);

        assertNotNull(checkExecutionList);
        assertEquals(0, checkExecutionList.size());
    }

    @Test
    public void getCheckExecutions_whenCheckNameProvidedOneHistorySaved_thenReturnsCheckExecutions() {
        String checkName = "check-name";
        int expectedSize = 1;
        BigDecimal lastResult = BigDecimal.valueOf(10);
        long lastExecutionTime = 10;
        checkExecutionRepository.save(CheckExecution.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(lastExecutionTime)
                .build());

        List<CheckExecution> checkExecutionList = underTest.getCheckExecutions(checkName);

        assertNotNull(checkExecutionList);
        assertEquals(expectedSize, checkExecutionList.size());
        assertEquals(checkName, checkExecutionList.get(0).getCheckName());
        assertEquals(0, lastResult.compareTo(checkExecutionList.get(0).getResult()));
        assertEquals(lastExecutionTime, checkExecutionList.get(0).getExecutionTime());
    }

    @Test
    public void getCheckExecutions_whenCheckNameProvidedMultipleHistoriesSaved_thenReturnsCheckExecutions() {
        String checkName = "check-name";
        int expectedSize = 3;
        checkExecutionRepository.saveAll(List.of(
                CheckExecution.builder().checkName(checkName).result(BigDecimal.valueOf(10)).executionTime(5L).build(),
                CheckExecution.builder().checkName(checkName).result(BigDecimal.valueOf(10)).executionTime(7L).build(),
                CheckExecution.builder().checkName(checkName).result(BigDecimal.valueOf(10)).executionTime(10L).build()
        ));

        List<CheckExecution> checkExecutionList = underTest.getCheckExecutions(checkName);

        assertNotNull(checkExecutionList);
        assertEquals(expectedSize, checkExecutionList.size());
    }


    @Test
    public void runCheck_whenCheckProvided_thenReturnsCheckResult() {
        Check check = createCheck(
                new CheckMetadata("check-name", "category"),
                "SELECT COUNT(*) FROM calculations"
        );
        BigDecimal result = BigDecimal.valueOf(14);

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getMetadata().getName(), checkResult.getMetadata().getName());
        assertEquals(check.getMetadata().getCategory(), checkResult.getMetadata().getCategory());
        assertNotNull(checkResult.getCheck());
        assertEquals(0, result.compareTo(checkResult.getCheck().getResult()));
        assertNotNull(checkResult.getCheck().getExecutionTime());
    }

    @Test
    public void runCheck_whenCheckErrorProvided_thenReturnsErrorCheckResult() {
        Check check = createNameErrorCheck(
                new CheckMetadata("check-name", "category"),
                "some error"
        );

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getMetadata().getName(), checkResult.getMetadata().getName());
        assertEquals(check.getMetadata().getCategory(), checkResult.getMetadata().getCategory());
        assertEquals(check.getError(), checkResult.getError());
    }

    @Test
    public void runCheck_whenCheckQueryIsNull_thenReturnsErrorCheckResult() {
        Check check = Check.builder()
                .metadata(new CheckMetadata("check-name", "category"))
                .build();

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getMetadata().getName(), checkResult.getMetadata().getName());
        assertEquals(check.getMetadata().getCategory(), checkResult.getMetadata().getCategory());
        assertEquals(TestedDatabaseException.MESSAGE, checkResult.getError());
    }

    @Test
    public void runCheck_whenCheckQueryIsEmpty_thenReturnsErrorCheckResult() {
        Check check = Check.builder()
                .metadata(new CheckMetadata("check-name", "category"))
                .query("")
                .build();

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getMetadata().getName(), checkResult.getMetadata().getName());
        assertEquals(check.getMetadata().getCategory(), checkResult.getMetadata().getCategory());
        assertEquals(TestedDatabaseException.MESSAGE, checkResult.getError());
    }


    @Test
    public void getQueryResult_whenQueryIsIncorrect_thenThrowsDatabaseBadSqlException() {
        String query = "not a query";

        assertThrows(DatabaseBadSqlException.class, () -> underTest.getQueryResult(query));
    }

    @Test
    public void getQueryResult_whenQueryProvided_thenReturnsQueryResult() {
        String query = "SELECT COUNT(*) FROM calculations";
        BigDecimal expectedResult = BigDecimal.valueOf(14);

        BigDecimal queryResult = underTest.getQueryResult(query);

        assertNotNull(queryResult);
        assertEquals(0, expectedResult.compareTo(queryResult));
    }


    @Test
    public void saveCheckToHistory_whenInsertCheckExecutionProvided_thenReturnsCheckExecutionDTO() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        long lastExecutionTime = 10;
        CheckExecution insertCheckExecution = CheckExecution.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(lastExecutionTime)
                .build();

        CheckExecutionDTO checkExecutionDTO = underTest.saveCheckToHistory(insertCheckExecution);

        assertNotNull(checkExecutionDTO);
        assertEquals(0, lastResult.compareTo(checkExecutionDTO.getResult()));
        assertEquals(lastExecutionTime, checkExecutionDTO.getExecutionTime());
        assertNotNull(checkExecutionDTO.getTimestamp());
    }


    @Test
    public void calculateTrend_whenSavedCheckExecution_thenReturnsCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10);
        BigDecimal lastResult = BigDecimal.valueOf(5);
        long lastExecutionTime = 1;
        checkExecutionRepository.save(CheckExecution.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(lastExecutionTime)
                .build());

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertEquals(0, lastResult.compareTo(checkTrend.getLastCheck().getResult()));
        assertEquals(100.0, checkTrend.getTrendPercentage());
        assertNotNull(checkTrend.getLastCheck());
        assertEquals(0, lastResult.compareTo(checkTrend.getLastCheck().getResult()));
        assertEquals(lastExecutionTime, checkTrend.getLastCheck().getExecutionTime());
        assertNotNull(checkTrend.getLastCheck().getTimestamp());
    }

    @Test
    public void calculateTrend_whenSavedCheckExecutionIsZero_thenReturnsCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10);
        BigDecimal lastResult = BigDecimal.valueOf(0);
        long lastExecutionTime = 1;
        checkExecutionRepository.save(CheckExecution.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(lastExecutionTime)
                .build());

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertEquals(0, lastResult.compareTo(checkTrend.getLastCheck().getResult()));
        assertNull(checkTrend.getTrendPercentage());
        assertNotNull(checkTrend.getLastCheck());
        assertEquals(0, lastResult.compareTo(checkTrend.getLastCheck().getResult()));
        assertEquals(lastExecutionTime, checkTrend.getLastCheck().getExecutionTime());
        assertNotNull(checkTrend.getLastCheck().getTimestamp());
    }

    @Test
    public void calculateTrend_whenNoSavedCheckExecution_thenReturnsEmptyCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10.0);

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertNull(checkTrend.getLastCheck());
    }

}