package com.example.newsBlock.restController;

import com.example.newsBlock.NewsBlockApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NewsCategoryControllerTest extends NewsBlockApplicationTest {

    @Test
    void createCategory() throws Exception {
        mockMvc.perform(post("/category/create")
                        .content(objectMapper.writeValueAsString(newsCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}