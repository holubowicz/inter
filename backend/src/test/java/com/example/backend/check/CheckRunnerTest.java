package com.example.backend.check;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CheckRunnerTest {

    @Autowired
    private CheckRunner underTest;

//    @Autowired
//    private ResultHistoryRepository resultHistoryRepository;

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

//    @BeforeEach
//    void setupBeforeEach() {
//        resultHistoryRepository.deleteAll();
//    }

    // TODO: create unit tests for CheckRunner.runCheck(Check check)

    @Test
    void runCheck() {
    }

    // TODO: create unit tests for CheckRunner.calculateTrend(String checkName, BigDecimal currentResult)

    @Test
    void calculateTrend() {
    }

    @Test
    void saveResultToHistory_whenCheckNameAndResultProvided_thenSaveResult() {
        String checkName = "check-name";
        BigDecimal result = BigDecimal.valueOf(10);

        assertDoesNotThrow(() ->
                underTest.saveResultToHistory(checkName, result)
        );
    }

    @Test
    void saveResultToHistory_whenCheckNameIsNull_thenThrowIllegalArgumentException() {
        BigDecimal result = BigDecimal.valueOf(10);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.saveResultToHistory(null, result)
        );

        assertTrue(exception.getMessage().contains(CheckRunner.CHECK_NAME_NULL_ERROR));
    }

    @Test
    void saveResultToHistory_whenCheckNameIsEmpty_thenThrowIllegalArgumentException() {
        String checkName = "";
        BigDecimal result = BigDecimal.valueOf(10);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.saveResultToHistory(checkName, result)
        );

        assertTrue(exception.getMessage().contains(CheckRunner.CHECK_NAME_EMPTY_ERROR));
    }

    @Test
    void saveResultToHistory_whenResultIsNull_thenThrowIllegalArgumentException() {
        String checkName = "check-name";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.saveResultToHistory(checkName, null)
        );

        assertTrue(exception.getMessage().contains(CheckRunner.RESULT_NULL_ERROR));
    }

}