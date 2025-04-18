package com.example.backend.query;

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
class QueryControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withInitScript("init.sql");

    @DynamicPropertySource
    static void setTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void getQueries_whenMockMvc_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(get("/api/queries"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].name").exists());
    }

    @Test
    void runQueries_whenNoBody_thenBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/queries/run"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void runQueries_whenSingleQuery_thenVerifyResponse() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"absolute-avg\"}]";
        final int EXPECTED_SIZE = 1;

        this.mockMvc.perform(post("/api/queries/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[0]").isArray())
                .andExpect(jsonPath("$[0][*].value").exists());
    }

    @Test
    void runQueries_whenMultipleQueries_thenVerifyResponse() throws Exception {
        final String REQUEST_BODY = "[{\"name\": \"absolute-avg\"},{\"name\": \"avg-all\"},{\"name\": \"total-count\"}]";
        final int EXPECTED_SIZE = 3;

        this.mockMvc.perform(post("/api/queries/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[*]", everyItem(not(empty()))))
                .andExpect(jsonPath("$[*][*].value").exists());
    }
}