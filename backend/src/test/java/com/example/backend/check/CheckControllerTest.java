package com.example.backend.check;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.backend.check.common.error.message.ApiErrorMessage.CHECK_METADATA_LIST_INCORRECT;
import static com.example.backend.check.common.error.message.ApiErrorMessage.CHECK_METADATA_LIST_ITEM_INCORRECT;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
class CheckControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void getCheckDTOs_whenRequestSent_thenVerifyResponseStructure() throws Exception {
        this.mockMvc.perform(get("/api/checks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].name", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].lastCheck").exists());
    }

    @Test
    public void getCheckDTOs_whenRequestSent_thenVerifyResultValues() throws Exception {
        final List<String> EXPECTED_NAMES = Arrays.asList(
                "absolute-avg",
                "avg-all",
                "negative-count",
                "positive-count",
                "total-count",
                "wrong-sql-format"
        );
        final int EXPECTED_SIZE = EXPECTED_NAMES.size();

        this.mockMvc.perform(get("/api/checks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[*].metadata.name", containsInAnyOrder(EXPECTED_NAMES.toArray())));
    }


    @Test
    public void getCheckExecutionDTOs_whenRequestSent_thenVerifyResponseStructure() throws Exception {
        this.mockMvc.perform(get("/api/checks/check-name/history"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    public void runCheckMetadataList_whenCheckMetadataListIsNotProvided_thenIsBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/checks/run"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(CHECK_METADATA_LIST_INCORRECT));
    }

    @Test
    public void runCheckMetadataList_whenCheckMetadataListIsEmpty_thenIsBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ArrayList<>().toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(CHECK_METADATA_LIST_INCORRECT));
    }

    @Test
    public void runCheckMetadataList_whenCheckMetadataListIsNotCheckMetadataType_thenIsBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"msg\":  \"bad request\"}]"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(CHECK_METADATA_LIST_ITEM_INCORRECT)
                );
    }

    @Test
    public void runCheckMetadataList_whenSingleCheckMetadataListProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"absolute-avg\", \"category\": \"good\"}]";
        final int EXPECTED_SIZE = 1;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[0].error", nullValue()))
                .andExpect(jsonPath("$[0].metadata.name", notNullValue()))
                .andExpect(jsonPath("$[0].metadata.category", notNullValue()))
                .andExpect(jsonPath("$[0].check.result", notNullValue()))
                .andExpect(jsonPath("$[0].check.executionTime", notNullValue()))
                .andExpect(jsonPath("$[0].check.timestamp", notNullValue()));
    }

    @Test
    public void runCheckMetadataList_whenSingleNotExistingCheckMetadataListProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"not-existing\", \"category\": \"category\"}]";
        final int EXPECTED_SIZE = 1;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[0].error", notNullValue()))
                .andExpect(jsonPath("$[0].trendPercentage", nullValue()))
                .andExpect(jsonPath("$[0].metadata.name", notNullValue()))
                .andExpect(jsonPath("$[0].metadata.category", notNullValue()))
                .andExpect(jsonPath("$[0].check", nullValue()))
                .andExpect(jsonPath("$[0].lastCheck", nullValue()));
    }

    @Test
    public void runCheckMetadataList_whenSingleWrongQueryFormatCheckMetadataListProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"wrong-sql-format\", \"category\": \"bad\"}]";
        final int EXPECTED_SIZE = 1;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[0].error", notNullValue()))
                .andExpect(jsonPath("$[0].trendPercentage", nullValue()))
                .andExpect(jsonPath("$[0].metadata.name", notNullValue()))
                .andExpect(jsonPath("$[0].metadata.category", notNullValue()))
                .andExpect(jsonPath("$[0].check", nullValue()));
    }

    @Test
    public void runCheckMetadataList_whenSingleCheckMetadataListProvided_thenVerifyResultValue() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"positive-count\", \"category\": \"good\"}]";

        final String EXPECTED_NAME = "positive-count";
        final String EXPECTED_CATEGORY = "good";
        final int EXPECTED_RESULT = 5;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].error", nullValue()))
                .andExpect(jsonPath("$[0].metadata.name", equalTo(EXPECTED_NAME)))
                .andExpect(jsonPath("$[0].metadata.category", equalTo(EXPECTED_CATEGORY)))
                .andExpect(jsonPath("$[0].check.result", equalTo(EXPECTED_RESULT)))
                .andExpect(jsonPath("$[0].check.executionTime", notNullValue()))
                .andExpect(jsonPath("$[0].check.timestamp", notNullValue()));
    }

    @Test
    public void runCheckMetadataList_whenMultipleCheckMetadataListProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[" +
                "{\"name\": \"absolute-avg\", \"category\": \"good\"}," +
                "{\"name\": \"avg-all\", \"category\": \"good\"}," +
                "{\"name\": \"negative-count\", \"category\": \"good\"}" +
                "]";
        final int EXPECTED_SIZE = 3;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$", everyItem(not(empty()))))
                .andExpect(jsonPath("$[*].error", everyItem(nullValue())))
                .andExpect(jsonPath("$[*].metadata.name", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].metadata.category", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].check.result", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].check.executionTime", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].check.timestamp", everyItem(notNullValue())));
    }

    @Test
    public void runCheckMetadataList_whenMultipleNotExistingCheckMetadataListProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[" +
                "{\"name\": \"not-existing\", \"category\": \"category\"}," +
                "{\"name\": \"not-existing\", \"category\": \"category\"}," +
                "{\"name\": \"not-existing\", \"category\": \"category\"}" +
                "]";
        final int EXPECTED_SIZE = 3;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$", everyItem(not(empty()))))
                .andExpect(jsonPath("$[*].error", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].metadata.name", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].metadata.category", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].check", everyItem(nullValue())))
                .andExpect(jsonPath("$[*].lastCheck", everyItem(nullValue())));
    }

    @Test
    public void runCheckMetadataList_whenMultipleWrongQueryFormatCheckMetadataListProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[" +
                "{\"name\": \"wrong-sql-format\", \"category\": \"bad\"}," +
                "{\"name\": \"wrong-sql-format\", \"category\": \"bad\"}" +
                "]";
        final int EXPECTED_SIZE = 2;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$", everyItem(not(empty()))))
                .andExpect(jsonPath("$[*].error", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].metadata.name", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].metadata.category", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].check", everyItem(nullValue())));
    }

    @Test
    public void runCheckMetadataList_whenMultipleCheckMetadataListProvided_thenVerifyResultValues() throws Exception {
        final String REQUEST_BODY = "[" +
                "{\"name\": \"negative-count\", \"category\": \"good\"}," +
                "{\"name\": \"positive-count\", \"category\": \"good\"}," +
                "{\"name\": \"total-count\", \"category\": \"good\"}" +
                "]";

        final String EXPECTED_NAME_1 = "negative-count";
        final String EXPECTED_CATEGORY_1 = "good";
        final int EXPECTED_RESULT_1 = 9;

        final String EXPECTED_NAME_2 = "positive-count";
        final String EXPECTED_CATEGORY_2 = "good";
        final int EXPECTED_RESULT_2 = 5;

        final String EXPECTED_NAME_3 = "total-count";
        final String EXPECTED_CATEGORY_3 = "good";
        final int EXPECTED_RESULT_3 = 14;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].metadata.name", equalTo(EXPECTED_NAME_1)))
                .andExpect(jsonPath("$[0].metadata.category", equalTo(EXPECTED_CATEGORY_1)))
                .andExpect(jsonPath("$[0].check.result", equalTo(EXPECTED_RESULT_1)))
                .andExpect(jsonPath("$[1].metadata.name", equalTo(EXPECTED_NAME_2)))
                .andExpect(jsonPath("$[1].metadata.category", equalTo(EXPECTED_CATEGORY_2)))
                .andExpect(jsonPath("$[1].check.result", equalTo(EXPECTED_RESULT_2)))
                .andExpect(jsonPath("$[2].metadata.name", equalTo(EXPECTED_NAME_3)))
                .andExpect(jsonPath("$[2].metadata.category", equalTo(EXPECTED_CATEGORY_3)))
                .andExpect(jsonPath("$[2].check.result", equalTo(EXPECTED_RESULT_3)));
    }


    @Test
    public void getCheckCategories_whenRequestSent_thenVerifyResponseStructure() throws Exception {
        this.mockMvc.perform(get("/api/checks/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*]", everyItem(notNullValue())));
    }

    @Test
    public void getCheckCategories_whenRequestSent_thenVerifyResultValues() throws Exception {
        final List<String> EXPECTED_CATEGORIES = Arrays.asList(
                "good",
                "bad"
        );

        this.mockMvc.perform(get("/api/checks/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*]", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(EXPECTED_CATEGORIES.toArray())));
    }


    // TODO: create integration test for runCheckCategories()

}