package com.example.backend.check;

import com.example.backend.check.common.exception.CheckInputDTOListNullException;
import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import com.example.backend.check.model.dto.CheckInputDTO;
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
    void getCheckDTOs_whenApplicationContext_thenReturnsCheckDTOs() {
        List<CheckDTO> checkDTOs = underTest.getCheckDTOs();

        assertNotNull(checkDTOs);
        checkDTOs.forEach(checkDTO -> {
            assertNotNull(checkDTO);
            assertNotNull(checkDTO.getName());
            assertFalse(checkDTO.getName().isEmpty());
        });
    }


    @Test
    void getCheckExecutionDTOs_whenCheckNameIsNull_thenThrowsNameNullException() {
        assertThrows(NameNullException.class, () ->
                underTest.getCheckExecutionDTOs(null)
        );
    }

    @Test
    void getCheckExecutionDTOs_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";

        assertThrows(NameEmptyException.class, () ->
                underTest.getCheckExecutionDTOs(checkName)
        );
    }

    @Test
    void getCheckExecutionDTOs_whenCheckNameIsBlankSpace_thenThrowsNameEmptyException() {
        String checkName = " ";

        assertThrows(NameEmptyException.class, () ->
                underTest.getCheckExecutionDTOs(checkName)
        );
    }

    @Test
    void getCheckExecutionDTOs_whenCheckNameProvided_thenReturnsCheckExecutionDTOs() {
        String checkName = "check-name";

        List<CheckExecutionDTO> checkExecutionDTOs = underTest.getCheckExecutionDTOs(checkName);

        assertNotNull(checkExecutionDTOs);
    }


    @Test
    void runCheckDTOs_whenCheckDTOListIsNull_thenThrowsCheckInputDTOListNullException() {
        assertThrows(CheckInputDTOListNullException.class, () ->
                underTest.runCheckDTOs(null)
        );
    }

    @Test
    void runCheckDTOs_whenCheckDTOListProvided_thenReturnsCheckResultList() {
        List<CheckInputDTO> checkInputDTOList = List.of(
                new CheckInputDTO("absolute-avg"),
                new CheckInputDTO("total-count")
        );

        List<CheckResult> checkResults = underTest.runCheckDTOs(checkInputDTOList);

        assertNotNull(checkResults);
        checkResults.forEach(checkResult -> {
            assertNotNull(checkResult);
            assertNotNull(checkResult.getName());
            assertFalse(checkResult.getName().isEmpty());
            assertNotNull(checkResult.getCheck());
            assertNotNull(checkResult.getCheck().getResult());
            assertNotNull(checkResult.getCheck().getTimestamp());
            assertNotNull(checkResult.getCheck().getExecutionTime());
        });
    }

}