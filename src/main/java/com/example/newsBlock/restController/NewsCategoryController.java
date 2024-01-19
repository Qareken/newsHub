package com.example.newsBlock.restController;

import com.example.newsBlock.mapper.NewsCategoryMapper;
import com.example.newsBlock.service.impl.NewsCategoryServiceImpl;
import com.example.newsBlock.service.impl.NewsServiceImpl;
import com.example.newsBlock.web.model.NewsCategoryDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class NewsCategoryController {
    private final NewsCategoryServiceImpl categoryService;
    private final NewsCategoryMapper categoryMapper;
    @Autowired
    public NewsCategoryController(NewsCategoryServiceImpl categoryService, NewsCategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }
    @PostMapping("/create")
    ResponseEntity<NewsCategoryDTO> createCategory(@Valid @RequestBody NewsCategoryDTO categoryDTO){
        var temp = categoryService.save(categoryMapper.toEntity(categoryDTO));

        return ResponseEntity.ok(categoryMapper.toDto(temp));
    }
}
