package com.example.newsBlock;



import com.example.newsBlock.web.model.NewsCategoryDTO;
import com.example.newsBlock.web.model.UpsertCommentDTO;
import com.example.newsBlock.web.model.UpsertNewsDTO;
import com.example.newsBlock.web.model.UpsertUserDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(initializers = {NewsBlockApplicationTest.Initializer.class})
@TestPropertySource(properties = {"spring.config.location=classpath:application.yml"})
public class NewsBlockApplicationTest {
    private static final String DATABASE_NAME = "app_db";
    private final String path ="src/main/resources/jsonTest.json";
    @Autowired
    protected MockMvc mockMvc;
    protected Long newsId;
    protected Long authorId;
    protected Long categoryId;


    protected UpsertUserDTO upsertUserDTO ;
    protected UpsertNewsDTO upsertNewsDTO;

    protected UpsertCommentDTO upsertCommentDTO;
    protected NewsCategoryDTO newsCategoryDTO;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withReuse(true)
            .withDatabaseName(DATABASE_NAME);

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "CONTAINER.USERNAME=" + postgreSQLContainer.getUsername(),
                    "CONTAINER.PASSWORD=" + postgreSQLContainer.getPassword(),
                    "CONTAINER.URL=" + postgreSQLContainer.getJdbcUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
    @BeforeEach
    public void setup() throws IOException {
        // Извлекаем данные пользователей
        String jsonContent = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
//        System.out.println(jsonContent);
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};

        Map<String, Object> data = objectMapper.readValue(jsonContent,typeRef );
        Optional.ofNullable(data.get("upsertUser"))
                .ifPresent(handleUser());
        Optional.ofNullable(data.get("categoryDTO")).ifPresent(handleCategoryDTO());
        Optional.ofNullable(data.get("upsertNewsDTO")).ifPresent(handleNewsDto());
        Optional.ofNullable(data.get("commentDto")).ifPresent(handleCommentDto());

 }
 private Consumer<Object> handleUser(){
     return userObject -> {
         upsertUserDTO = objectMapper.convertValue(userObject, UpsertUserDTO.class);
     };
 }
 private Consumer<Object> handleCategoryDTO(){
        return categoryObject->{
            newsCategoryDTO = objectMapper.convertValue(categoryObject, NewsCategoryDTO.class);
        };
 }
    private Consumer<Object> handleNewsDto(){
        return newsObject->{
            upsertNewsDTO = objectMapper.convertValue(newsObject, UpsertNewsDTO.class);
        };
    }
    private Consumer<Object> handleCommentDto(){
        return commentObject->{
            upsertCommentDTO = objectMapper.convertValue(commentObject, UpsertCommentDTO.class);
        };
    }



}