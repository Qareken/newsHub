package com.example.newsBlock.service;

import com.example.newsBlock.entity.Comments;
import com.example.newsBlock.entity.News;
import com.example.newsBlock.entity.Users;

import java.util.List;

public interface UsersService {
    // Сохранение пользователя
    Users save(Users user);

    // Поиск пользователя по идентификатору
    Users findById(Long id);

    // Поиск пользователя по email
    Users findByEmail(String email);

    // Получение списка всех пользователей
    List<Users> findAll();

    // Удаление пользователя по идентификатору
    void deleteById(Long id);
    void delete(Users users);

    Users update(Users user);
}