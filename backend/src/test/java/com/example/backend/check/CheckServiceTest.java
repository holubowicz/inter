package com.example.backend.check;

import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.CheckInputDto;
import com.example.backend.check.model.CheckResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CheckServiceTest {

    @Autowired
    private CheckService underTest;

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


    @Test
    void getCheckDtoList_whenApplicationContext_thenReturnCheckDtoList() {
        List<CheckDto> checkInputDtoList = underTest.getCheckDtoList();

        assertNotNull(checkInputDtoList);
        checkInputDtoList.forEach(checkDto -> {
            assertNotNull(checkDto);
            assertNotNull(checkDto.getName());
            assertFalse(checkDto.getName().isEmpty());
        });
    }


    @Test
    void runCheckDtoList_whenCheckDtoListIsNull_thenReturnCheckResultList() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.runCheckDtoList(null)
        );

        assertTrue(exception.getMessage().contains(CheckService.CHECK_DTO_LIST_NULL_ERROR));
    }

    @Test
    void runCheckDtoList_whenCheckDtoListProvided_thenReturnCheckResultList() {
        List<CheckInputDto> checkInputDtoList = List.of(
                new CheckInputDto("absolute-avg"),
                new CheckInputDto("total-count")
        );

        List<CheckResult> results = underTest.runCheckDtoList(checkInputDtoList);

        assertNotNull(results);
        results.forEach(checkResult -> {
            assertNotNull(checkResult);
            assertNotNull(checkResult.getName());
            assertFalse(checkResult.getName().isEmpty());
            assertNotNull(checkResult.getExecutionTime());
        });
    }

}