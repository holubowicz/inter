package com.example.backend.check;

import com.example.backend.check.common.exception.ExecutionTimeNullException;
import com.example.backend.check.common.exception.ResultNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.CheckTrend;
import com.example.backend.check.model.factory.CheckFactory;
import com.example.backend.database.schema.CheckHistory;
import com.example.backend.database.schema.CheckHistoryRepository;
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

import static com.example.backend.check.common.ErrorMessages.FAILED_QUERY_DB;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CheckRunnerTest {

    @Autowired
    private CheckRunner underTest;

    @Autowired
    private CheckHistoryRepository checkHistoryRepository;

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
        checkHistoryRepository.deleteAll();
    }


    @Test
    void getCheckDto_whenCheckNameIsNull_thenThrowsNameNullException() {
        assertThrows(NameNullException.class, () ->
                underTest.getCheckDto(null)
        );
    }

    @Test
    void getCheckDto_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";

        assertThrows(NameEmptyException.class, () ->
                underTest.getCheckDto(checkName)
        );
    }

    @Test
    void getCheckDto_whenNoResultHistorySaved_thenReturnsNotFullCheckDto() {
        String checkName = "check-name";

        CheckDto checkDto = underTest.getCheckDto(checkName);

        assertNotNull(checkDto);
        assertEquals(checkName, checkDto.getName());
        assertNull(checkDto.getLastResult());
        assertNull(checkDto.getLastTimestamp());
        assertNull(checkDto.getLastExecutionTime());
    }

    @Test
    void getCheckDto_whenResultHistorySaved_thenReturnsNotFullCheckDto() {
        String checkName = "check-name";
        BigDecimal lastResult = BigDecimal.valueOf(10);
        Long lastExecutionTime = 10L;
        checkHistoryRepository.save(CheckHistory.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(lastExecutionTime)
                .build()
        );

        CheckDto checkDto = underTest.getCheckDto(checkName);

        assertNotNull(checkDto);
        assertEquals(checkName, checkDto.getName());
        assertEquals(0, lastResult.compareTo(checkDto.getLastResult()));
        assertNotNull(checkDto.getLastTimestamp());
        assertEquals(lastExecutionTime, checkDto.getLastExecutionTime());
    }


    @Test
    void getCheckHistoryList_whenCheckNameIsNull_thenThrowsNameNullException() {
        assertThrows(NameNullException.class, () ->
                underTest.getCheckHistoryList(null)
        );
    }

    @Test
    void getCheckHistoryList_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";

        assertThrows(NameEmptyException.class, () ->
                underTest.getCheckHistoryList(checkName)
        );
    }

    @Test
    void getCheckHistoryList_whenCheckNameProvidedNoHistorySaved_thenReturnsEmptyCheckHistoryList() {
        String checkName = "check-name";

        List<CheckHistory> checkHistoryList = underTest.getCheckHistoryList(checkName);

        assertNotNull(checkHistoryList);
        assertEquals(0, checkHistoryList.size());
    }

    @Test
    void getCheckHistoryList_whenCheckNameProvidedOneHistorySaved_thenReturnsCheckHistoryList() {
        String checkName = "check-name";
        int expectedSize = 1;
        BigDecimal lastResult = BigDecimal.valueOf(10);
        long lastExecutionTime = 10;
        checkHistoryRepository.save(CheckHistory.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(lastExecutionTime)
                .build());

        List<CheckHistory> checkHistoryList = underTest.getCheckHistoryList(checkName);

        assertNotNull(checkHistoryList);
        assertEquals(expectedSize, checkHistoryList.size());
        assertEquals(checkName, checkHistoryList.get(0).getCheckName());
        assertEquals(0, lastResult.compareTo(checkHistoryList.get(0).getResult()));
        assertEquals(lastExecutionTime, checkHistoryList.get(0).getExecutionTime());
    }

    @Test
    void getCheckHistoryList_whenCheckNameProvidedMultipleHistoriesSaved_thenReturnsCheckHistoryList() {
        String checkName = "check-name";
        int expectedSize = 3;
        checkHistoryRepository.saveAll(List.of(
                CheckHistory.builder().checkName(checkName).result(BigDecimal.valueOf(10)).executionTime(5L).build(),
                CheckHistory.builder().checkName(checkName).result(BigDecimal.valueOf(11)).executionTime(7L).build(),
                CheckHistory.builder().checkName(checkName).result(BigDecimal.valueOf(12)).executionTime(10L).build()
        ));

        List<CheckHistory> checkHistoryList = underTest.getCheckHistoryList(checkName);

        assertNotNull(checkHistoryList);
        assertEquals(expectedSize, checkHistoryList.size());
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
        Check check = CheckFactory.createCheck("check-name", "SELECT COUNT(*) FROM calculations;");
        BigDecimal result = BigDecimal.valueOf(14);

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getName(), checkResult.getName());
        assertEquals(0, result.compareTo(checkResult.getResult()));
        assertNotNull(checkResult.getExecutionTime());
    }

    @Test
    void runCheck_whenCheckErrorProvided_thenReturnsErrorCheckResult() {
        Check check = CheckFactory.createNameErrorCheck("check-name", "some error");

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
    void calculateTrend_whenSavedResultHistory_thenReturnsCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10);
        BigDecimal lastResult = BigDecimal.valueOf(5);
        long executionTime = 1;
        checkHistoryRepository.save(CheckHistory.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(executionTime)
                .build());

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertEquals(0, lastResult.compareTo(checkTrend.getLastResult()));
        assertEquals(100.0, checkTrend.getTrendPercentage());
    }

    @Test
    void calculateTrend_whenSavedResultHistoryIsZero_thenReturnsCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10);
        BigDecimal lastResult = BigDecimal.valueOf(0);
        long executionTime = 1;
        checkHistoryRepository.save(CheckHistory.builder()
                .checkName(checkName)
                .result(lastResult)
                .executionTime(executionTime)
                .build());

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertEquals(0, lastResult.compareTo(checkTrend.getLastResult()));
        assertNull(checkTrend.getTrendPercentage());
    }

    @Test
    void calculateTrend_whenNoSavedResultHistory_thenReturnsEmptyCheckTrend() {
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