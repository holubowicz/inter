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

import static com.example.backend.check.common.error.message.ApiErrorMessage.CHECK_INPUT_DTO_LIST_INCORRECT;
import static com.example.backend.check.common.error.message.ApiErrorMessage.CHECK_INPUT_DTO_LIST_ITEM_INCORRECT;
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
                .andExpect(jsonPath("$[*].lastResult").exists())
                .andExpect(jsonPath("$[*].lastTimestamp").exists());
    }

    @Test
    public void getCheckDTOs_whenRequestSent_thenVerifyResultValues() throws Exception {
        List<String> EXPECTED_NAMES = Arrays.asList(
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
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(EXPECTED_NAMES.toArray())));
    }


    @Test
    public void getCheckExecutionDTOs_whenRequestSent_thenVerifyResponseStructure() throws Exception {
        this.mockMvc.perform(get("/api/checks/check-name/history"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    public void runCheckDTOs_whenCheckDTOsIsNotProvided_thenIsBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/checks/run"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(CHECK_INPUT_DTO_LIST_INCORRECT));
    }

    @Test
    public void runCheckDTOs_whenCheckDTOsIsEmpty_thenIsBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ArrayList<>().toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(CHECK_INPUT_DTO_LIST_INCORRECT));
    }

    @Test
    public void runCheckDTOs_whenCheckDTOsIsNotCheckDTOType_thenIsBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"msg\":  \"bad request\"}]"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(CHECK_INPUT_DTO_LIST_ITEM_INCORRECT)
                );
    }

    @Test
    public void runCheckDTOs_whenSingleCheckDTOProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"absolute-avg\"}]";
        final int EXPECTED_SIZE = 1;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].error", nullValue()))
                .andExpect(jsonPath("$[0].result", notNullValue()))
                .andExpect(jsonPath("$[0].executionTime", notNullValue()));
    }

    @Test
    public void runCheckDTOs_whenSingleNotExistingCheckDTOProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"not-existing\"}]";
        final int EXPECTED_SIZE = 1;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].error", notNullValue()))
                .andExpect(jsonPath("$[0].result", nullValue()))
                .andExpect(jsonPath("$[0].executionTime", nullValue()))
                .andExpect(jsonPath("$[0].lastResult", nullValue()))
                .andExpect(jsonPath("$[0].lastTimestamp", nullValue()))
                .andExpect(jsonPath("$[0].trendPercentage", nullValue()));
    }

    @Test
    public void runCheckDTOs_whenSingleWrongQueryFormatCheckDTOProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"wrong-sql-format\"}]";
        final int EXPECTED_SIZE = 1;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].error", notNullValue()))
                .andExpect(jsonPath("$[0].result", nullValue()))
                .andExpect(jsonPath("$[0].executionTime", nullValue()))
                .andExpect(jsonPath("$[0].lastResult", nullValue()))
                .andExpect(jsonPath("$[0].lastTimestamp", nullValue()))
                .andExpect(jsonPath("$[0].trendPercentage", nullValue()));
    }

    @Test
    public void runCheckDTOs_whenSingleCheckDTOProvided_thenVerifyResultValue() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"positive-count\"}]";

        final String EXPECTED_NAME = "positive-count";
        final int EXPECTED_RESULT = 5;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", equalTo(EXPECTED_NAME)))
                .andExpect(jsonPath("$[0].error", nullValue()))
                .andExpect(jsonPath("$[0].result", equalTo(EXPECTED_RESULT)))
                .andExpect(jsonPath("$[0].executionTime", notNullValue()));
    }

    @Test
    public void runCheckDTOs_whenMultipleCheckDTOsProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"absolute-avg\"},{\"name\": \"avg-all\"},{\"name\": \"negative-count\"}]";
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
                .andExpect(jsonPath("$[*].name", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].error", everyItem(nullValue())))
                .andExpect(jsonPath("$[*].result", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].executionTime", everyItem(notNullValue())));
    }

    @Test
    public void runCheckDTOs_whenMultipleNotExistingCheckDTOsProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"not-existing-1\"},{\"name\": \"not-existing-1\"},{\"name\": \"not-existing-1\"}]";
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
                .andExpect(jsonPath("$[*].name", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].error", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].result", everyItem(nullValue())))
                .andExpect(jsonPath("$[*].executionTime", everyItem(nullValue())));
    }

    @Test
    public void runCheckDTOs_whenMultipleWrongQueryFormatCheckDTOsProvided_thenVerifyResponseStructure() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"wrong-sql-format\"},{\"name\": \"wrong-sql-format\"}]";
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
                .andExpect(jsonPath("$[*].name", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].error", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].result", everyItem(nullValue())))
                .andExpect(jsonPath("$[*].executionTime", everyItem(nullValue())));
    }

    @Test
    public void runCheckDTOs_whenMultipleCheckDTOsProvided_thenVerifyResultValues() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"negative-count\"},{\"name\": \"positive-count\"},{\"name\": \"total-count\"}]";

        final String EXPECTED_NAME_1 = "negative-count";
        final int EXPECTED_RESULT_1 = 9;

        final String EXPECTED_NAME_2 = "positive-count";
        final int EXPECTED_RESULT_2 = 5;

        final String EXPECTED_NAME_3 = "total-count";
        final int EXPECTED_RESULT_3 = 14;

        this.mockMvc.perform(post("/api/checks/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", equalTo(EXPECTED_NAME_1)))
                .andExpect(jsonPath("$[0].result", equalTo(EXPECTED_RESULT_1)))
                .andExpect(jsonPath("$[1].name", equalTo(EXPECTED_NAME_2)))
                .andExpect(jsonPath("$[1].result", equalTo(EXPECTED_RESULT_2)))
                .andExpect(jsonPath("$[2].name", equalTo(EXPECTED_NAME_3)))
                .andExpect(jsonPath("$[2].result", equalTo(EXPECTED_RESULT_3)));
    }

}