package com.example.fantasy.mapper;

import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.entity.Role;
import com.example.fantasy.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserMapperTest {

    @Test
    public void testMapping() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("ac@abc.com");
        newUserDTO.setPassword("123");

        User result = userMapper.toUser(newUserDTO, passwordEncoder);

        assertThat(result, allOf(
                hasProperty("id", is(nullValue())),
                hasProperty("role", is(Role.ROLE_USER)),
                hasProperty("email", is("ac@abc.com")),
                hasProperty("blocked", is(false)),
                hasProperty("failedLoginAttempts", is(0)),
                hasProperty("team", is(nullValue()))
        ));
        assertThat(passwordEncoder.matches("123", result.getPasswordHash()), is(true));
    }

}
