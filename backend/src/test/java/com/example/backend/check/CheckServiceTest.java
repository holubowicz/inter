package com.example.backend.check;

import com.example.backend.check.common.exception.CheckCategoriesNullException;
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
    public void getCheckDTOs_whenApplicationContext_thenReturnsCheckDTOs() {
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
    public void getCheckExecutionDTOs_whenCheckNameIsNullOrEmpty_thenThrowsNameNullOrEmptyException(String checkName) {
        assertThrows(NameNullOrEmptyException.class, () ->
                underTest.getCheckExecutionDTOs(checkName)
        );
    }

    @Test
    public void getCheckExecutionDTOs_whenCheckNameProvided_thenReturnsCheckExecutionDTOs() {
        String checkName = "check-name";

        List<CheckExecutionDTO> checkExecutionDTOs = underTest.getCheckExecutionDTOs(checkName);

        assertNotNull(checkExecutionDTOs);
    }


    @Test
    public void runCheckMetadataList_whenCheckMetadataListIsNull_thenThrowsCheckMetadataListNullException() {
        assertThrows(CheckMetadataListNullException.class, () ->
                underTest.runCheckMetadataList(null)
        );
    }

    @Test
    public void runCheckMetadataList_whenCheckDTOListProvided_thenReturnsCheckResultList() {
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


    @Test
    public void getCheckCategories_whenApplicationContext_thenReturnsCheckCategories() {
        List<String> categories = underTest.getCheckCategories();

        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        categories.forEach(category -> {
            assertNotNull(category);
            assertFalse(category.isEmpty());
        });
    }


    @Test
    public void runCheckCategories_whenCategoriesIsNull_thenThrowsCheckCategoriesNullException() {
        assertThrows(CheckCategoriesNullException.class, () -> underTest.runCheckCategories(null));
    }

    @Test
    public void runCheckCategories_whenCategoriesIsEmpty_thenReturnsEmptyList() {
        List<String> categories = List.of();

        List<CheckResult> results = underTest.runCheckCategories(categories);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void runCheckCategories_whenCategoriesProvided_thenReturnsCheckResultList() {
        String category = "good";
        List<String> categories = List.of(category);

        List<CheckResult> results = underTest.runCheckCategories(categories);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        results.forEach(result -> {
            assertNotNull(result);
            assertNotNull(result.getMetadata());
            assertNotNull(result.getMetadata().getName());
            assertEquals(category, result.getMetadata().getCategory());
        });
    }

}