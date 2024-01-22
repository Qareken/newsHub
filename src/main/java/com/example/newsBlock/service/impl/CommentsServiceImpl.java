package com.example.newsBlock.service.impl;

import com.example.newsBlock.Exception.EntityNotFoundException;
import com.example.newsBlock.entity.Comments;
import com.example.newsBlock.entity.News;
import com.example.newsBlock.entity.Users;
import com.example.newsBlock.repository.CommentsRepository;
import com.example.newsBlock.aop.CommentValid;
import com.example.newsBlock.service.CommentsService;

import com.example.newsBlock.service.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;
    @Override
    public Comments save(Comments comment) {
        return commentsRepository.save(comment);
    }

    @Override
    public Comments findById(Long id) {
        return commentsRepository.findById(id).orElseThrow(()->new EntityNotFoundException(MessageFormat.format("Comment with {0} not founded", id)));
    }


    @Override
    @CommentValid
    public void deleteById(Long id, Long userId) {
        commentsRepository.deleteById(id);
    }


    @Override
    @CommentValid
    public Comments update(Comments comments) {
        var existedComment= commentsRepository.findCommentsByContentAndNews(comments.getContent(), comments.getUsers());
        BeanUtils.copyNonNullProperties(comments, existedComment);
        return commentsRepository.save(existedComment);
    }
}
