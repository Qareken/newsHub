package com.example.newsBlock.service;

import com.example.newsBlock.entity.NewsCategory;
import com.example.newsBlock.entity.enumurated.Category;

import java.util.List;

public interface NewsCategoryService {
    // Сохранение категории новостей
    NewsCategory save(NewsCategory category);

    // Поиск категории по идентификатору
    NewsCategory findById(Long id);

    // Получение списка всех категорий
    List<NewsCategory> findAll();

    // Удаление категории по идентификатору
    void deleteById(Long id);

    // Найти категорию по типу (например, по Category enum)
    NewsCategory findByCategory(Category category);
    NewsCategory update(NewsCategory newsCategory);
    // Другие методы, если необходимо, в соответствии с вашими требованиями
}
