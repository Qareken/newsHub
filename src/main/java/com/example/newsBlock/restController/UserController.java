package com.example.newsBlock.restController;

import com.example.newsBlock.mapper.UserMapper;
import com.example.newsBlock.service.impl.UsersServiceImpl;
import com.example.newsBlock.web.model.UpsertUserDTO;
import com.example.newsBlock.web.model.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
public class UserController {
    private final UsersServiceImpl usersService;
    private final UserMapper userMapper;
    @Autowired
    public UserController(UsersServiceImpl usersService, UserMapper userMapper) {
        this.usersService = usersService;
        this.userMapper = userMapper;
    }
    @PostMapping("/create")
    public ResponseEntity<UserDTO>createUser(@Valid @RequestBody UpsertUserDTO upsertUserDTO){
        var user = userMapper.toEntity(upsertUserDTO);
        return ResponseEntity.ok(userMapper.toDto(usersService.save(user)));
    }
    @PutMapping("/edit")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UpsertUserDTO upsertUserDTO){
        var user = userMapper.toEntity(upsertUserDTO);
        return ResponseEntity.ok(userMapper.toDto(usersService.update(user)));
    }
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email){
        var user = usersService.findByEmail(email);
        usersService.delete(user);
        return ResponseEntity.ok().build();
    }

}
