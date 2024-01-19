package com.example.newsBlock.restController;

import com.example.newsBlock.mapper.CommentMapper;
import com.example.newsBlock.service.impl.CommentsServiceImpl;
import com.example.newsBlock.web.model.CommentDTO;
import com.example.newsBlock.web.model.UpsertCommentDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/comments")
@Slf4j
public class CommentsController {
    private final CommentMapper commentMapper;
    private final CommentsServiceImpl service;

    public CommentsController(CommentMapper commentMapper, CommentsServiceImpl service) {
        this.commentMapper = commentMapper;
        this.service = service;
    }

    @PostMapping("/create")
    ResponseEntity<CommentDTO> createComment(@Valid @RequestBody UpsertCommentDTO commentDTO) {
        var comment = commentMapper.toEntity(commentDTO);
        log.info(comment.getUsers().getLastName()+" "+comment.getNews().getId());
        return ResponseEntity.ok().body(commentMapper.toDto(service.save(comment)));
    }
    @PutMapping("/update")
    ResponseEntity<CommentDTO> update(@Valid @RequestBody UpsertCommentDTO commentDTO){
        var comment = commentMapper.toEntity(commentDTO);
        return ResponseEntity.ok().body(commentMapper.toDto(service.update(comment)));
    }
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String userIdString =  request.getHeader("UserId");
        if (userIdString == null || userIdString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserId header is missing");
        }

        Long userId;
        try {

            userId = Long.parseLong(userIdString);
            log.info("userId "+userId);
            service.deleteById(id, userId);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UserId format");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
