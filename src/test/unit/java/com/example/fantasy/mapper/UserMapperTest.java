package com.example.fantasy.mapper;

import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapperTest {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testMapping() {
        UserMapper userMapper = new UserMapperImpl();
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("ac@abc.com");
        newUserDTO.setPassword("123");
        newUserDTO.setTeamName("PSG");

        User result = userMapper.toUser(newUserDTO, passwordEncoder);

        //todo assertions
        System.out.println(result);
    }

}
