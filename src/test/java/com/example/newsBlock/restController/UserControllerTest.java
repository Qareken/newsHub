package com.example.newsBlock.restController;


import com.example.newsBlock.Exception.EntityNotFoundException;
import com.example.newsBlock.NewsBlockApplicationTest;

import com.example.newsBlock.entity.Users;
import com.example.newsBlock.mapper.UserMapper;
import com.example.newsBlock.service.impl.UsersServiceImpl;

import com.example.newsBlock.web.model.UpsertUserDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends NewsBlockApplicationTest {



    @Autowired
    private UsersServiceImpl service;

//    @BeforeEach
//    void setUp() throws Exception {
//        String email=upsertUserDTO.getEmail();
//        mockMvc.perform(delete("/user/delete/{email}", email));
//    }

    @Test
    void createUser() throws Exception {


    mockMvc.perform(post("/user/create")
                        .content(objectMapper.writeValueAsString(upsertUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    void deleteUser() throws Exception {
        String email=upsertUserDTO.getEmail();
        mockMvc.perform(delete("/user/delete/{email}", email))
                .andExpect(status().isOk());
        assertThrows(EntityNotFoundException.class, () -> {
            service.findByEmail(email);
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        mockMvc.perform(post("/user/create")
                        .content(objectMapper.writeValueAsString(upsertUserDTO))
                        .contentType(MediaType.APPLICATION_JSON));
        upsertNewsDTO.getAuthor().setId(service.findByEmail(upsertUserDTO.getEmail()).getId());
    }


}