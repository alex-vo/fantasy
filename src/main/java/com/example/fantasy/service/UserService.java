package com.example.fantasy.service;

import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.entity.User;
import com.example.fantasy.mapper.UserMapper;
import com.example.fantasy.repository.admin.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void createUser(NewUserDTO newUserDTO) {
        User user = userMapper.toUser(newUserDTO);
        userRepository.save(user);
    }

}
