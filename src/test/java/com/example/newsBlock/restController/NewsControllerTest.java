package com.example.newsBlock.restController;


import com.example.newsBlock.NewsBlockApplicationTest;
import com.example.newsBlock.entity.News;
import com.example.newsBlock.mapper.NewsMapper;
import com.example.newsBlock.service.impl.NewsCategoryServiceImpl;
import com.example.newsBlock.service.impl.NewsServiceImpl;
import com.example.newsBlock.service.impl.UsersServiceImpl;
import com.example.newsBlock.web.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class NewsControllerTest extends NewsBlockApplicationTest {


    @Autowired
    private NewsServiceImpl newsService;
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private NewsCategoryServiceImpl categoryService;


    @Test

    void createNews() throws Exception {
        upsertNewsDTO.getAuthor().setId(usersService.findByEmail(upsertNewsDTO.getAuthor().getEmail()).getId());
        upsertNewsDTO.getCategories().forEach(a->{
            a.setId(categoryService.findByCategory(a.getCategoryName()).getId());
        });
        MvcResult mvcResult = mockMvc.perform(post("/news/create")
                        .content(objectMapper.writeValueAsString(upsertNewsDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(upsertNewsDTO.getTitle())))
                .andReturn();
        String responseContent = mvcResult.getResponse().getContentAsString();
//        System.out.println(responseContent);

        // Десериализация тела ответа в объект DTO
        NewsDetailDTO   responseNewsDTO = objectMapper.readValue(responseContent, NewsDetailDTO.class);
        System.out.println("newsDetail: "+responseNewsDTO);
        // Извлечение ID из объекта DTO
        newsId = responseNewsDTO.getId();
        System.out.println(newsId);



        authorId = responseNewsDTO.getAuthor().getId();
        System.out.println("auhtor "+authorId);
        // Добавьте дополнительные проверки для других полей, если необходимо
    }

    @Test

    void findById() throws Exception {
        newsId = 19L;
        mockMvc.perform(get("/news/{id}", newsId))
                .andExpect(status().isOk()) // Проверка, что статус ответа 200 OK
                .andExpect(jsonPath("$.id", is(newsId.intValue()))) // Проверка, что ID в ответе соответствует ожидаемому
                .andExpect(jsonPath("$.title", is("Spring boot updated to 3 version"))); // Проверка названия новости
             // Проверка содержания новости
        // ... другие проверки, если нужны ...

    }

    @Test

    void findAll() throws Exception {
        int page = 0;
        int size = 10;
        mockMvc.perform(get("/news/findAll")
                        .param("page", String.valueOf(page)) // Установка номера страницы
                        .param("size", String.valueOf(size)) // Установка размера страницы
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Проверка, что статус ответа 200 OK
                .andExpect(jsonPath("$.size", is(size))) // Проверка размера страницы
                .andExpect(jsonPath("$.number", is(page))) // Проверка номера страницы
                .andExpect(jsonPath("$.content", hasSize(lessThanOrEqualTo(size))));
    }

    @Test

    void findByFilter() throws Exception {
        int page = 0;
        int size = 10;

        // Создаем объект фильтра
        NewsFilter newsFilter = new NewsFilter();
        // newsFilter.setSomeField("someValue");
        newsFilter.setAuthorName(upsertNewsDTO.getAuthor().getFirstName());

        mockMvc.perform(get("/news/findFilter")
                        .content(objectMapper.writeValueAsString(newsFilter)) // Установка тела запроса с фильтром
                        .param("page", String.valueOf(page)) // Установка номера страницы
                        .param("size", String.valueOf(size)) // Установка размера страницы
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Проверка, что статус ответа 200 OK
                .andExpect(jsonPath("$.size", is(size))) // Проверка размера страницы
                .andExpect(jsonPath("$.number", is(page))) // Проверка номера страницы
                .andExpect(jsonPath("$.content", hasSize(lessThanOrEqualTo(size)))); // Проверка, что количество новостей не превышает размер страницы
        // ... другие проверки, если нужны ...
    }

    @Test

    void deleteNewsWithValidUserId() throws Exception {
        authorId = 62L;
        newsId = 19L;

        mockMvc.perform(delete("/news/delete/{id}", newsId)
                        .header("UserId", authorId) // установка заголовка UserId
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // проверка, что статус ответа 200 OK
    }

    @Test

    void deleteNewsWithMissingUserId() throws Exception {
        Long newsId = 1L; // предположим, что новость с таким ID существует

        mockMvc.perform(delete("/news/delete/{id}", newsId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;// проверка, что статус ответа 400 Bad Request
    }

    @Test

    void deleteNewsWithInvalidUserIdFormat() throws Exception {
        Long newsId = 1L; // предположим, что новость с таким ID существует
        String invalidUserId = "abc"; // невалидный формат UserId

        mockMvc.perform(delete("/news/delete/{id}", newsId)
                        .header("UserId", invalidUserId) // установка некорректного UserId
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;// проверка, что статус ответа 400 Bad Request
                // проверка тела ответа
    }
}