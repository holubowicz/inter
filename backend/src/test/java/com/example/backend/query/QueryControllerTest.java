package com.example.backend.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class QueryControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

//    @AfterEach
//    void tearDown() {
//    }

    @Test
    void getQueries_whenMockMvc_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(get("/api/queries"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.name)]").isArray());
    }

    @Test
    void runQueries_whenNoBody_thenBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/queries/run"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void runQueries_whenSingleQuery_thenVerifyResponse() throws Exception {
        String body = "[{\"name\": \"absolute-avg\"}]";

        this.mockMvc.perform(post("/api/queries/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@[0].value)]").isArray());
    }

    @Test
    void runQueries_whenMultipleQueries_thenVerifyResponse() throws Exception {
        String body = "[{\"name\": \"absolute-avg\"},{\"name\": \"avg-all\"},{\"name\": \"total-count\"}]";

        this.mockMvc.perform(post("/api/queries/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@[0].value)]").isArray())
                .andExpect(jsonPath("$[?(@[0].value)]", hasSize(3)));
    }
}