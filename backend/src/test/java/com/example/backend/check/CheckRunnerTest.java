package com.example.backend.check;

import com.example.backend.check.common.exception.NameNullOrEmptyException;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckExecution;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.CheckTrend;
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

import static com.example.backend.check.common.error.message.DatabaseErrorMessage.FAILED_QUERY_DB;
import static com.example.backend.check.model.factory.CheckExecutionFactory.createInsertCheckExecution;
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
    void setupBeforeEach() {
        checkExecutionRepository.deleteAll();
    }


    @ParameterizedTest
    @NullAndEmptySource
    void getCheckDTO_whenCheckNameIsNull_thenThrowsNameNullOrEmptyException(String checkName) {
        assertThrows(NameNullOrEmptyException.class, () ->
                underTest.getCheckDTO(checkName)
        );
    }

    @Test
    void getCheckDTO_whenNoCheckExecutionSaved_thenReturnsNotFullCheckDTO() {
        String checkName = "check-name";

        CheckDTO checkDTO = underTest.getCheckDTO(checkName);

        assertNotNull(checkDTO);
        assertEquals(checkName, checkDTO.getName());
        assertNull(checkDTO.getLastCheck());
    }

    @Test
    void getCheckDTO_whenCheckExecutionSaved_thenReturnsNotFullCheckDTO() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Long lastExecutionTime = 10L;
        checkExecutionRepository.save(createInsertCheckExecution(checkName, lastResult, lastExecutionTime));

        CheckDTO checkDTO = underTest.getCheckDTO(checkName);

        assertNotNull(checkDTO);
        assertEquals(checkName, checkDTO.getName());
        assertNotNull(checkDTO.getLastCheck());
        assertEquals(0, lastResult.compareTo(checkDTO.getLastCheck().getResult()));
        assertNotNull(checkDTO.getLastCheck().getTimestamp());
        assertEquals(lastExecutionTime, checkDTO.getLastCheck().getExecutionTime());
    }


    @ParameterizedTest
    @NullAndEmptySource
    void getCheckExecutions_whenCheckNameIsNull_thenThrowsNameNullOrEmptyException(String checkName) {
        assertThrows(NameNullOrEmptyException.class, () ->
                underTest.getCheckExecutions(checkName)
        );
    }

    @Test
    void getCheckExecutions_whenCheckNameProvidedNoHistorySaved_thenReturnsEmptyCheckExecutions() {
        String checkName = "check-name";

        List<CheckExecution> checkExecutionList = underTest.getCheckExecutions(checkName);

        assertNotNull(checkExecutionList);
        assertEquals(0, checkExecutionList.size());
    }

    @Test
    void getCheckExecutions_whenCheckNameProvidedOneHistorySaved_thenReturnsCheckExecutions() {
        String checkName = "check-name";
        int expectedSize = 1;
        BigDecimal lastResult = BigDecimal.valueOf(10);
        long lastExecutionTime = 10;
        checkExecutionRepository.save(createInsertCheckExecution(checkName, lastResult, lastExecutionTime));

        List<CheckExecution> checkExecutionList = underTest.getCheckExecutions(checkName);

        assertNotNull(checkExecutionList);
        assertEquals(expectedSize, checkExecutionList.size());
        assertEquals(checkName, checkExecutionList.get(0).getCheckName());
        assertEquals(0, lastResult.compareTo(checkExecutionList.get(0).getResult()));
        assertEquals(lastExecutionTime, checkExecutionList.get(0).getExecutionTime());
    }

    @Test
    void getCheckExecutions_whenCheckNameProvidedMultipleHistoriesSaved_thenReturnsCheckExecutions() {
        String checkName = "check-name";
        int expectedSize = 3;
        checkExecutionRepository.saveAll(List.of(
                createInsertCheckExecution(checkName, BigDecimal.valueOf(10), 5L),
                createInsertCheckExecution(checkName, BigDecimal.valueOf(11), 7L),
                createInsertCheckExecution(checkName, BigDecimal.valueOf(12), 10L)
        ));

        List<CheckExecution> checkExecutionList = underTest.getCheckExecutions(checkName);

        assertNotNull(checkExecutionList);
        assertEquals(expectedSize, checkExecutionList.size());
    }


    @Test
    void runCheck_whenCheckProvided_thenReturnsCheckResult() {
        Check check = createCheck("check-name", "SELECT COUNT(*) FROM calculations;");
        BigDecimal result = BigDecimal.valueOf(14);

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getName(), checkResult.getName());
        assertNotNull(checkResult.getCheck());
        assertEquals(0, result.compareTo(checkResult.getCheck().getResult()));
        assertNotNull(checkResult.getCheck().getExecutionTime());
    }

    @Test
    void runCheck_whenCheckErrorProvided_thenReturnsErrorCheckResult() {
        Check check = createNameErrorCheck("check-name", "some error");

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getName(), checkResult.getName());
        assertEquals(check.getError(), checkResult.getError());
    }

    @Test
    void runCheck_whenCheckQueryIsNull_thenReturnsErrorCheckResult() {
        Check check = Check.builder().name("check-name").build();

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getName(), checkResult.getName());
        assertEquals(FAILED_QUERY_DB, checkResult.getError());
    }

    @Test
    void runCheck_whenCheckQueryIsEmpty_thenReturnsErrorCheckResult() {
        Check check = Check.builder()
                .name("check-name")
                .query("")
                .build();

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getName(), checkResult.getName());
        assertEquals(FAILED_QUERY_DB, checkResult.getError());
    }


    @Test
    void getQueryResult_whenQueryIsIncorrect_thenReturnsNull() {
        String query = "not a query";

        BigDecimal queryResult = underTest.getQueryResult(query);

        assertNull(queryResult);
    }

    @Test
    void getQueryResult_whenQueryProvided_thenReturnsQueryResult() {
        String query = "SELECT COUNT(*) FROM calculations";
        BigDecimal expectedResult = BigDecimal.valueOf(14);

        BigDecimal queryResult = underTest.getQueryResult(query);

        assertNotNull(queryResult);
        assertEquals(0, expectedResult.compareTo(queryResult));
    }


    @Test
    void getCheckTrend_whenNoCheckExecutionSaved_thenReturnsEmptyCheckTrend() {
        CheckTrend checkTrend = underTest.getCheckTrend("check-name", BigDecimal.valueOf(10));

        assertNotNull(checkTrend);
        assertNull(checkTrend.getTrendPercentage());
        assertNull(checkTrend.getLastCheck());
    }

    @Test
    void getCheckTrend_whenCheckExecutionSaved_thenReturnsEmptyCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10);
        BigDecimal lastResult = BigDecimal.valueOf(5);
        long lastExecutionTime = 10;
        checkExecutionRepository.save(createInsertCheckExecution(checkName, lastResult, lastExecutionTime));

        CheckTrend checkTrend = underTest.getCheckTrend("check-name", currentResult);

        assertNotNull(checkTrend);
        assertEquals(100.0, checkTrend.getTrendPercentage());
        assertNotNull(checkTrend.getLastCheck());
        assertEquals(0, lastResult.compareTo(checkTrend.getLastCheck().getResult()));
        assertEquals(lastExecutionTime, checkTrend.getLastCheck().getExecutionTime());
        assertNotNull(checkTrend.getLastCheck().getTimestamp());
    }


    @Test
    void saveCheckToHistory_whenInsertCheckExecutionProvided_thenReturnsCheckExecutionDTO() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        long lastExecutionTime = 10;
        CheckExecution insertCheckExecution = createInsertCheckExecution(checkName, lastResult, lastExecutionTime);

        CheckExecutionDTO checkExecutionDTO = underTest.saveCheckToHistory(insertCheckExecution);

        assertNotNull(checkExecutionDTO);
        assertEquals(0, lastResult.compareTo(checkExecutionDTO.getResult()));
        assertEquals(lastExecutionTime, checkExecutionDTO.getExecutionTime());
        assertNotNull(checkExecutionDTO.getTimestamp());
    }


    @Test
    void calculateTrend_whenSavedCheckExecution_thenReturnsCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10);
        BigDecimal lastResult = BigDecimal.valueOf(5);
        long executionTime = 1;
        checkExecutionRepository.save(createInsertCheckExecution(checkName, lastResult, executionTime));

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertEquals(0, lastResult.compareTo(checkTrend.getLastCheck().getResult()));
        assertEquals(100.0, checkTrend.getTrendPercentage());
        assertNotNull(checkTrend.getLastCheck());
        assertEquals(0, lastResult.compareTo(checkTrend.getLastCheck().getResult()));
        assertEquals(executionTime, checkTrend.getLastCheck().getExecutionTime());
        assertNotNull(checkTrend.getLastCheck().getTimestamp());
    }

    @Test
    void calculateTrend_whenSavedCheckExecutionIsZero_thenReturnsCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10);
        BigDecimal lastResult = BigDecimal.valueOf(0);
        long executionTime = 1;
        checkExecutionRepository.save(createInsertCheckExecution(checkName, lastResult, executionTime));

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertEquals(0, lastResult.compareTo(checkTrend.getLastCheck().getResult()));
        assertNull(checkTrend.getTrendPercentage());
        assertNotNull(checkTrend.getLastCheck());
        assertEquals(0, lastResult.compareTo(checkTrend.getLastCheck().getResult()));
        assertEquals(executionTime, checkTrend.getLastCheck().getExecutionTime());
        assertNotNull(checkTrend.getLastCheck().getTimestamp());
    }

    @Test
    void calculateTrend_whenNoSavedCheckExecution_thenReturnsEmptyCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10.0);

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertNull(checkTrend.getLastCheck());
    }

}