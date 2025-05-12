package com.example.backend.check;

import com.example.backend.check.common.exception.CheckMetadataListNullException;
import com.example.backend.check.common.exception.NameNullOrEmptyException;
import com.example.backend.check.model.CheckMetadata;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
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
            assertNotNull(checkDTO.getMetadata().getName());
            assertFalse(checkDTO.getMetadata().getName().isEmpty());
        });
    }


    @ParameterizedTest
    @NullAndEmptySource
    void getCheckExecutionDTOs_whenCheckNameIsNullOrEmpty_thenThrowsNameNullOrEmptyException(String checkName) {
        assertThrows(NameNullOrEmptyException.class, () ->
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
        assertThrows(CheckMetadataListNullException.class, () ->
                underTest.runCheckMetadataList(null)
        );
    }

    @Test
    void runCheckDTOs_whenCheckDTOListProvided_thenReturnsCheckResultList() {
        List<CheckMetadata> metadataList = List.of(
                new CheckMetadata("absolute-avg", "good"),
                new CheckMetadata("total-count", "good")
        );

        List<CheckResult> checkResults = underTest.runCheckMetadataList(metadataList);

        assertNotNull(checkResults);
        checkResults.forEach(checkResult -> {
            assertNotNull(checkResult);
            assertNotNull(checkResult.getMetadata().getName());
            assertFalse(checkResult.getMetadata().getName().isEmpty());
            assertNotNull(checkResult.getMetadata().getCategory());
            assertFalse(checkResult.getMetadata().getCategory().isEmpty());
            assertNotNull(checkResult.getCheck());
            assertNotNull(checkResult.getCheck().getResult());
            assertNotNull(checkResult.getCheck().getTimestamp());
            assertNotNull(checkResult.getCheck().getExecutionTime());
        });
    }

}