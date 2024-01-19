package com.example.newsBlock.service.impl;


import com.example.newsBlock.Exception.EntityNotFoundException;
import com.example.newsBlock.entity.Users;
import com.example.newsBlock.repository.UserRepository;
import com.example.newsBlock.service.UsersService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
@RequiredArgsConstructor
@Service
public class UsersServiceImpl implements UsersService {
    private final UserRepository userRepository;
    @Override
    public Users save(Users user) {
        return userRepository.save(user);
    }

    @Override
    public Users findById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new EntityNotFoundException(MessageFormat.format("user with {0} id not founded", id)));
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findUsersByEmail(email).orElseThrow(()->new EntityNotFoundException("User NOT found with this id"));
    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);

    }

    @Override
    public void delete(Users users) {
        userRepository.delete(users);
    }

    @Override
    public Users update(Users user) {
        var existUser = findByEmail(user.getEmail());
        existUser.setFirstName(user.getFirstName());
        existUser.setLastName(user.getLastName());
        existUser.setPassword(user.getPassword());
        return save(existUser);
    }


}
