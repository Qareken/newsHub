package com.example.newsBlock.restController;

import com.example.newsBlock.mapper.NewsMapper;
import com.example.newsBlock.service.impl.NewsServiceImpl;
import com.example.newsBlock.web.model.NewsDTO;
import com.example.newsBlock.web.model.NewsDetailDTO;
import com.example.newsBlock.web.model.NewsFilter;
import com.example.newsBlock.web.model.UpsertNewsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@RestController
@RequestMapping("/news")
@Slf4j
public class NewsController {
    private final NewsServiceImpl service;
    private final NewsMapper newsMapper;

    public NewsController(NewsServiceImpl service, NewsMapper newsMapper) {
        this.service = service;
        this.newsMapper = newsMapper;
    }
    @PostMapping("/create")
    public ResponseEntity<NewsDetailDTO>createNews(@Valid @RequestBody UpsertNewsDTO newsDTO){
         var news  = newsMapper.toEntity(newsDTO);
        log.info(news.getAuthor().getLastName());
         return ResponseEntity.ok().body(newsMapper.toDetailDto(service.save(news)));
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<NewsDetailDTO> findById(@PathVariable Long id){
        var news = service.findById(id);
        log.info(news.toString());

        return ResponseEntity.ok().body(newsMapper.toDetailDto(news));
    }
    @GetMapping("/findAll")
    public ResponseEntity<Page<NewsDTO>> findAll(Pageable pageable){
        return ResponseEntity.ok().body(service.findAll(pageable));

    }
    @GetMapping("/findFilter")
    public ResponseEntity<Page<NewsDTO>> findByFilter(Pageable pageable, @RequestBody NewsFilter newsFilter){
        return ResponseEntity.ok().body(service.findByFilter(pageable, newsFilter));
    }
    @PutMapping("/update")
    public ResponseEntity<NewsDetailDTO> updateNews(@Valid @RequestBody UpsertNewsDTO upsertNewsDTO, Long newsId){
        var news  = newsMapper.toEntity(upsertNewsDTO);
        return ResponseEntity.ok().body(newsMapper.toDetailDto(service.update(newsId, news) ));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id){
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
