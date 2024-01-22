package com.example.newsBlock.service.impl;

import com.example.newsBlock.Exception.EntityNotFoundException;
import com.example.newsBlock.aop.CommentValid;
import com.example.newsBlock.entity.News;
import com.example.newsBlock.entity.NewsCategory;
import com.example.newsBlock.entity.Users;
import com.example.newsBlock.mapper.NewsMapper;
import com.example.newsBlock.repository.NewsRepository;
import com.example.newsBlock.aop.NewsCheckAnnotation;
import com.example.newsBlock.repository.NewsSpecification;
import com.example.newsBlock.service.NewsService;

import com.example.newsBlock.service.utils.BeanUtils;
import com.example.newsBlock.web.model.NewsDTO;
import com.example.newsBlock.web.model.NewsFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Override
    public News save(News news) {

        return newsRepository.save(news);

    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("News with this {0} not founded", id)));
    }

    @Override
    public Page<NewsDTO> findAll(Pageable pageable) {
        var newsPage = newsRepository.findAll(pageable);
        return newsPage.map(newsMapper::toDto);
    }


    @NewsCheckAnnotation
    @Override
    public void deleteById(Long id, Long userId) {
        newsRepository.deleteById(id);
    }

    @Override
    public Page<NewsDTO> findByFilter(Pageable pageable, NewsFilter newsFilter) {
        var filterNews = newsRepository.findAll(NewsSpecification.withFilter(newsFilter), pageable);
        return filterNews.map(newsMapper::toDto);
    }


    @Override
    @CommentValid
    public News update(Long newsId,News news) {
        var existedNews = findById(newsId);
        BeanUtils.copyNonNullProperties(news, existedNews);
        return newsRepository.save(existedNews);
    }
}
