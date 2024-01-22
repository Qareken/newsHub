package com.example.newsBlock.service;

import com.example.newsBlock.entity.Comments;
import com.example.newsBlock.entity.News;
import com.example.newsBlock.entity.Users;

import java.util.List;

public interface UsersService {
    // Сохранение пользователя
    Users save(Users user);

    // Поиск пользователя по идентификатору


    // Поиск пользователя по email
    Users findByEmail(String email);

    // Получение списка всех пользователей

    // Удаление пользователя по идентификатору

    void delete(Users users);

    Users update(Users user);
}