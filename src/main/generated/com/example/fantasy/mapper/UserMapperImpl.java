package com.example.fantasy.mapper;

import com.example.fantasy.dto.UserDTO;
import com.example.fantasy.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-07-26T19:51:35+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.7 (Azul Systems, Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( userDTO.getEmail() );

        return user;
    }
}
