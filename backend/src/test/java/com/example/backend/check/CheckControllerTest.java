package com.example.backend.check;

import com.example.backend.check.loader.CheckLoaderConfiguration;
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

    @Autowired
    private CheckLoaderConfiguration checkLoaderConfiguration;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withInitScript("com/example/backend/check/init.sql");

    @DynamicPropertySource
    static void setTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        checkLoaderConfiguration.setChecksPath("src/test/resources/com/example/backend/check/checks");
    }

    @Test
    void getCheckDtoList_whenRequestSent_thenVerifyIfResponseStructure() throws Exception {
        this.mockMvc.perform(get("/api/checks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].name").exists());
    }

    @Test
    void getCheckDtoList_whenRequestSent_thenVerifyResultValues() throws Exception {
        this.mockMvc.perform(get("/api/checks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].name", hasItems(
                        "absolute-avg",
                        "avg-all",
                        "negative-count",
                        "positive-count",
                        "total-count"
                )));
    }

    @Test
    void runCheckDtoList_whenNoCheckDtoProvided_thenIsBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/checks/run"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void runCheckDtoList_whenSingleCheckDtoProvided_thenVerifyResponseStructure() throws Exception {
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
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].result").exists());
    }

    @Test
    void runCheckDtoList_whenSingleCheckDtoProvided_thenVerifyResultValue() throws Exception {
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
                .andExpect(jsonPath("$[0].result", equalTo(EXPECTED_RESULT)));
    }

    @Test
    void runCheckDtoList_whenMultipleCheckDtosProvided_thenVerifyResponseStructure() throws Exception {
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
                .andExpect(jsonPath("$[*].name").exists())
                .andExpect(jsonPath("$[*].result").exists());
    }

    @Test
    void runCheckDtoList_whenMultipleCheckDtosProvided_thenVerifyResultValues() throws Exception {
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