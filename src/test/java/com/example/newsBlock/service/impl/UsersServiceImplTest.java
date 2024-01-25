package com.example.newsBlock.service.impl;

import com.example.newsBlock.NewsBlockApplicationTest;
import com.example.newsBlock.entity.Users;
import com.example.newsBlock.mapper.UserMapper;
import com.example.newsBlock.web.model.UpsertUserDTO;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

class UsersServiceImplTest extends NewsBlockApplicationTest {
    @Autowired
    protected UserMapper userMapper;

   @Autowired
    protected  UsersServiceImpl service;



    @Test
    protected void save() {
        UpsertUserDTO upsertUserDTO = new UpsertUserDTO();
        upsertUserDTO.setFirstName("Sardar");
        upsertUserDTO.setLastName("Kudiyarov");
        upsertUserDTO.setEmail("sardar12@gmail.com");
        upsertUserDTO.setPassword("1234Aziz$");
        assertThrows(DataIntegrityViolationException.class, () -> {
            service.save(userMapper.toEntity(upsertUserDTO));
        });

    }
    @Test
    protected void findByEmail(){
        String email = "sardar122@gmail.com";
        Users found = service.findByEmail(email);

        assertNotNull(found, "User should be found");
        assertEquals("First names should match", found.getFirstName(), "Sardar");
        assertNotEquals("Auezov", found.getLastName());
    }


}