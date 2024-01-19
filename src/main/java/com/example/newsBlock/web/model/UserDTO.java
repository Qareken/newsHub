package com.example.newsBlock.web.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Data
@Getter
@Setter
@FieldNameConstants
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
