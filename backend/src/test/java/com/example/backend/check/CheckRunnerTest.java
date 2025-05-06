package com.example.backend.check;

import com.example.backend.check.common.exception.ExecutionTimeNullException;
import com.example.backend.check.common.exception.ResultNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.CheckTrend;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.database.schema.CheckExecution;
import com.example.backend.database.schema.CheckExecutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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


    @Test
    void getCheckDTO_whenCheckNameIsNull_thenThrowsNameNullException() {
        assertThrows(NameNullException.class, () ->
                underTest.getCheckDTO(null)
        );
    }

    @Test
    void getCheckDTO_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";

        assertThrows(NameEmptyException.class, () ->
                underTest.getCheckDTO(checkName)
        );
    }

    @Test
    void getCheckDTO_whenNoCheckExecutionSaved_thenReturnsNotFullCheckDTO() {
        String checkName = "check-name";

        CheckDTO checkDTO = underTest.getCheckDTO(checkName);

        assertNotNull(checkDTO);
        assertEquals(checkName, checkDTO.getName());
        assertNull(checkDTO.getLastResult());
        assertNull(checkDTO.getLastTimestamp());
        assertNull(checkDTO.getLastExecutionTime());
    }

    @Test
    void getCheckDTO_whenCheckExecutionSaved_thenReturnsNotFullCheckDTO() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Long lastExecutionTime = 10L;
        checkExecutionRepository.save(CheckExecution.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(lastExecutionTime)
                .build()
        );

        CheckDTO checkDTO = underTest.getCheckDTO(checkName);

        assertNotNull(checkDTO);
        assertEquals(checkName, checkDTO.getName());
        assertEquals(0, lastResult.compareTo(checkDTO.getLastResult()));
        assertNotNull(checkDTO.getLastTimestamp());
        assertEquals(lastExecutionTime, checkDTO.getLastExecutionTime());
    }


    @Test
    void getCheckExecutions_whenCheckNameIsNull_thenThrowsNameNullException() {
        assertThrows(NameNullException.class, () ->
                underTest.getCheckExecutions(null)
        );
    }

    @Test
    void getCheckExecutions_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";

        assertThrows(NameEmptyException.class, () ->
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
    void getCheckExecutions_whenCheckNameProvidedMultipleHistoriesSaved_thenReturnsCheckExecutions() {
        String checkName = "check-name";
        int expectedSize = 3;
        checkExecutionRepository.saveAll(List.of(
                CheckExecution.builder().checkName(checkName).result(BigDecimal.valueOf(10)).executionTime(5L).build(),
                CheckExecution.builder().checkName(checkName).result(BigDecimal.valueOf(11)).executionTime(7L).build(),
                CheckExecution.builder().checkName(checkName).result(BigDecimal.valueOf(12)).executionTime(10L).build()
        ));

        List<CheckExecution> checkExecutionList = underTest.getCheckExecutions(checkName);

        assertNotNull(checkExecutionList);
        assertEquals(expectedSize, checkExecutionList.size());
    }


    @Test
    void runCheck_whenCheckNameIsNull_thenThrowsNameNullException() {
        Check check = Check.builder()
                .query("SELECT COUNT(*) FROM calculations;")
                .build();

        assertThrows(NameNullException.class, () ->
                underTest.runCheck(check)
        );
    }

    @Test
    void runCheck_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        Check check = Check.builder()
                .name("")
                .query("SELECT COUNT(*) FROM calculations;")
                .build();

        assertThrows(NameEmptyException.class, () ->
                underTest.runCheck(check)
        );
    }

    @Test
    void runCheck_whenCheckProvided_thenReturnsCheckResult() {
        Check check = createCheck("check-name", "SELECT COUNT(*) FROM calculations;");
        BigDecimal result = BigDecimal.valueOf(14);

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getName(), checkResult.getName());
        assertEquals(0, result.compareTo(checkResult.getResult()));
        assertNotNull(checkResult.getExecutionTime());
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
    void calculateTrend_whenCheckNameIsNull_thenThrowsNameNullException() {
        BigDecimal currentResult = BigDecimal.valueOf(10.0);

        assertThrows(NameNullException.class, () ->
                underTest.calculateTrend(null, currentResult)
        );
    }

    @Test
    void calculateTrend_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";
        BigDecimal currentResult = BigDecimal.valueOf(10.0);

        assertThrows(NameEmptyException.class, () ->
                underTest.calculateTrend(checkName, currentResult)
        );
    }

    @Test
    void calculateTrend_whenCurrentResultIsNull_thenThrowsResultNullException() {
        String checkName = "check-name";

        assertThrows(ResultNullException.class, () ->
                underTest.calculateTrend(checkName, null)
        );
    }

    @Test
    void calculateTrend_whenSavedCheckExecution_thenReturnsCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10);
        BigDecimal lastResult = BigDecimal.valueOf(5);
        long executionTime = 1;
        checkExecutionRepository.save(CheckExecution.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(executionTime)
                .build());

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertEquals(0, lastResult.compareTo(checkTrend.getLastResult()));
        assertEquals(100.0, checkTrend.getTrendPercentage());
    }

    @Test
    void calculateTrend_whenSavedCheckExecutionIsZero_thenReturnsCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10);
        BigDecimal lastResult = BigDecimal.valueOf(0);
        long executionTime = 1;
        checkExecutionRepository.save(CheckExecution.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(executionTime)
                .build());

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertEquals(0, lastResult.compareTo(checkTrend.getLastResult()));
        assertNull(checkTrend.getTrendPercentage());
    }

    @Test
    void calculateTrend_whenNoSavedCheckExecution_thenReturnsEmptyCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10.0);

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertNull(checkTrend.getLastResult());
        assertNull(checkTrend.getTrendPercentage());
    }


    @Test
    void saveResultToHistory_whenCheckNameIsNull_thenThrowsNameNullException() {
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 1;

        assertThrows(NameNullException.class, () ->
                underTest.saveResultToHistory(null, result, executionTime)
        );
    }

    @Test
    void saveResultToHistory_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 1;

        assertThrows(NameEmptyException.class, () ->
                underTest.saveResultToHistory(checkName, result, executionTime)
        );
    }

    @Test
    void saveResultToHistory_whenResultIsNull_thenThrowsResultNullException() {
        String checkName = "check-name";
        long executionTime = 1;

        assertThrows(ResultNullException.class, () ->
                underTest.saveResultToHistory(checkName, null, executionTime)
        );
    }

    @Test
    void saveResultToHistory_whenExecutionTimeIsNull_thenThrowsExecutionTimeNullException() {
        String checkName = "check-name";
        BigDecimal result = BigDecimal.valueOf(10);

        assertThrows(ExecutionTimeNullException.class, () ->
                underTest.saveResultToHistory(checkName, result, null)
        );
    }

    @Test
    void saveResultToHistory_whenCheckNameAndResultProvided_thenSaveResult() {
        String checkName = "check-name";
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 1;

        assertDoesNotThrow(() ->
                underTest.saveResultToHistory(checkName, result, executionTime)
        );
    }

}