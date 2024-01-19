package com.example.newsBlock.aop;

import com.example.newsBlock.Exception.EntityNotFoundException;
import com.example.newsBlock.Exception.ValidException;
import com.example.newsBlock.entity.News;
import com.example.newsBlock.service.impl.CommentsServiceImpl;
import com.example.newsBlock.service.impl.NewsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MyAspectAop {
    private final NewsServiceImpl service;
    private final CommentsServiceImpl commentsService;
    @Autowired
    public MyAspectAop(NewsServiceImpl service, CommentsServiceImpl commentsService) {
        this.service = service;
        this.commentsService = commentsService;
    }

    @Before(value = "@annotation(CommentValid)&&args(commentId,userId,..)", argNames = "commentId,userId")
    public void checkCommentOwnerShip(Long commentId,Long userId ){
        if(!userId.equals(commentsService.findById(commentId).getUsers().getId())){
            throw new ValidException("У вас нет прав на выполнение этой операции.");
        }
    }
    @Before(value = "@annotation(NewsCheckAnnotation)&&args(newsId,userId,..)", argNames = "newsId,userId")
    public void checksNewsOwnerShip(Long newsId, Long userId){
        try {
            News news = service.findById(newsId); // Это может выбросить EntityNotFoundException
            if (!userId.equals(news.getAuthor().getId())) {
                throw new ValidException("У вас нет прав на выполнение этой операции.");
            }
        } catch (EntityNotFoundException ex) {
            log.error("EntityNotFoundException: ", ex);
            throw ex; // Переброс исключения для обработки в GlobalExceptionHandler
        }
    }

}
