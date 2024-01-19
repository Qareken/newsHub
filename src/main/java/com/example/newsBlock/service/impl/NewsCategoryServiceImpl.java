package com.example.newsBlock.service.impl;

import com.example.newsBlock.Exception.CustomDuplicateException;
import com.example.newsBlock.Exception.EntityNotFoundException;
import com.example.newsBlock.entity.NewsCategory;
import com.example.newsBlock.entity.enumurated.Category;
import com.example.newsBlock.repository.NewsCategoryRepository;
import com.example.newsBlock.service.NewsCategoryService;
import com.example.newsBlock.service.utils.BeanUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsCategoryServiceImpl implements NewsCategoryService {
    private final NewsCategoryRepository newsCategoryRepository;
    @Override
    public NewsCategory save(NewsCategory category) {
        try {
            return newsCategoryRepository.save(category);
        }catch (DataIntegrityViolationException e){
            throw new CustomDuplicateException( "unique constraint with: "+category.getCategory().getLabel());
        }
    }
    @Override
    public NewsCategory findById(Long id) {
        return newsCategoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(MessageFormat.format("Category with {0} not founded", id)));
    }
    @Override
    public List<NewsCategory> findAll() {
        return newsCategoryRepository.findAll();
    }
    @Override
    public void deleteById(Long id) {
        newsCategoryRepository.deleteById(id);
    }
    @Override
    public NewsCategory findByCategory(Category category) {
        return newsCategoryRepository.findNewsCategoryByCategory(category);
    }
    @Override
    public NewsCategory update(NewsCategory newsCategory) {
        var existedCategory = findById(newsCategory.getId());
        BeanUtils.copyNonNullProperties(newsCategory, existedCategory);
        return newsCategoryRepository.save(existedCategory);
    }
}