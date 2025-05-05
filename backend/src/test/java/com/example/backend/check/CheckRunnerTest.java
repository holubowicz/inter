package com.example.backend.check;

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

import static com.example.backend.check.common.ApiErrorMessages.FAILED_QUERY_DB;
import static com.example.backend.check.common.ErrorMessages.*;
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
    void getCheckDto_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.getCheckDto(null)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_NULL));
    }

    @Test
    void getCheckDto_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.getCheckDto(checkName)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_EMPTY));
    }

    @Test
    void getCheckDto_whenNoResultHistorySaved_thenReturnNotFullCheckDto() {
        String checkName = "check-name";

        CheckDto checkDto = underTest.getCheckDto(checkName);

        assertNotNull(checkDto);
        assertEquals(checkName, checkDto.getName());
        assertNull(checkDto.getLastResult());
        assertNull(checkDto.getLastTimestamp());
        assertNull(checkDto.getLastExecutionTime());
    }

    @Test
    void getCheckDto_whenResultHistorySaved_thenReturnNotFullCheckDto() {
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
    void getCheckHistoryList_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.getCheckHistoryList(null)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_NULL));
    }

    @Test
    void getCheckHistoryList_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.getCheckHistoryList(checkName)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_EMPTY));
    }

    @Test
    void getCheckHistoryList_whenCheckNameProvidedNoHistorySaved_thenReturnEmptyCheckHistoryList() {
        String checkName = "check-name";

        List<CheckHistory> checkHistoryList = underTest.getCheckHistoryList(checkName);

        assertNotNull(checkHistoryList);
        assertEquals(0, checkHistoryList.size());
    }

    @Test
    void getCheckHistoryList_whenCheckNameProvidedOneHistorySaved_thenReturnCheckHistoryList() {
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
    void getCheckHistoryList_whenCheckNameProvidedMultipleHistoriesSaved_thenReturnCheckHistoryList() {
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
    void runCheck_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        Check check = Check.builder()
                .query("SELECT COUNT(*) FROM calculations;")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.runCheck(check)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_NULL));
    }

    @Test
    void runCheck_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        Check check = Check.builder()
                .name("")
                .query("SELECT COUNT(*) FROM calculations;")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.runCheck(check)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_EMPTY));
    }

    @Test
    void runCheck_whenCheckProvided_thenReturnCheckResult() {
        Check check = CheckFactory.createCheck("check-name", "SELECT COUNT(*) FROM calculations;");
        BigDecimal result = BigDecimal.valueOf(14);

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getName(), checkResult.getName());
        assertEquals(0, result.compareTo(checkResult.getResult()));
        assertNotNull(checkResult.getExecutionTime());
    }

    @Test
    void runCheck_whenCheckErrorProvided_thenReturnErrorCheckResult() {
        Check check = CheckFactory.createNameErrorCheck("check-name", "some error");

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getName(), checkResult.getName());
        assertEquals(check.getError(), checkResult.getError());
    }

    @Test
    void runCheck_whenCheckQueryIsNull_thenReturnErrorCheckResult() {
        Check check = Check.builder().name("check-name").build();

        CheckResult checkResult = underTest.runCheck(check);

        assertNotNull(checkResult);
        assertEquals(check.getName(), checkResult.getName());
        assertEquals(FAILED_QUERY_DB, checkResult.getError());
    }

    @Test
    void runCheck_whenCheckQueryIsEmpty_thenReturnErrorCheckResult() {
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
    void calculateTrend_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        BigDecimal currentResult = BigDecimal.valueOf(10.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateTrend(null, currentResult)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_NULL));
    }

    @Test
    void calculateTrend_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";
        BigDecimal currentResult = BigDecimal.valueOf(10.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateTrend(checkName, currentResult)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_EMPTY));
    }

    @Test
    void calculateTrend_whenCurrentResultIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateTrend(checkName, null)
        );

        assertTrue(exception.getMessage().contains(RESULT_NULL));
    }

    @Test
    void calculateTrend_whenSavedResultHistory_thenReturnCheckTrend() {
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
    void calculateTrend_whenSavedResultHistoryIsZero_thenReturnCheckTrend() {
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
    void calculateTrend_whenNoSavedResultHistory_thenReturnEmptyCheckTrend() {
        String checkName = "check-name";
        BigDecimal currentResult = BigDecimal.valueOf(10.0);

        CheckTrend checkTrend = underTest.calculateTrend(checkName, currentResult);

        assertNull(checkTrend.getLastResult());
        assertNull(checkTrend.getTrendPercentage());
    }


    @Test
    void saveResultToHistory_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.saveResultToHistory(null, result, executionTime)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_NULL));
    }

    @Test
    void saveResultToHistory_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";
        BigDecimal result = BigDecimal.valueOf(10);
        long executionTime = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.saveResultToHistory(checkName, result, executionTime)
        );

        assertTrue(exception.getMessage().contains(CHECK_NAME_EMPTY));
    }

    @Test
    void saveResultToHistory_whenResultIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";
        long executionTime = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.saveResultToHistory(checkName, null, executionTime)
        );

        assertTrue(exception.getMessage().contains(RESULT_NULL));
    }

    @Test
    void saveResultToHistory_whenExecutionTimeIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";
        BigDecimal result = BigDecimal.valueOf(10);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.saveResultToHistory(checkName, result, null)
        );

        assertTrue(exception.getMessage().contains(EXECUTION_TIME_NULL));
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