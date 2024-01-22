package com.example.newsBlock.restController;
import com.example.newsBlock.entity.News;
import com.example.newsBlock.mapper.NewsMapper;
import com.example.newsBlock.service.impl.NewsServiceImpl;
import com.example.newsBlock.web.model.NewsDetailDTO;
import com.example.newsBlock.web.model.UpsertNewsDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@WebMvcTest(NewsController.class)
@AutoConfigureMockMvc
public  class NewsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsServiceImpl service;

    @MockBean
    private NewsMapper newsMapper;

    @Test
    public void testCreateNews() throws Exception {
        UpsertNewsDTO newsDTO = new UpsertNewsDTO();
        // заполните newsDTO данными

        NewsDetailDTO detailDTO = new NewsDetailDTO();
        // заполните detailDTO данными

        when(newsMapper.toEntity(any(UpsertNewsDTO.class))).thenReturn(new News());
        when(service.save(any(News.class))).thenReturn(new News());
        when(newsMapper.toDetailDto(any(News.class))).thenReturn(detailDTO);

        mockMvc.perform(post("/news/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(detailDTO.getTitle())));
    }

    // Функция для преобразования объекта в JSON строку
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
