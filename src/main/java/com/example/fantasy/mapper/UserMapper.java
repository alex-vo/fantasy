package com.example.fantasy.mapper;

import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.entity.User;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "blocked", constant = "false")
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "role", constant = "ROLE_USER")
    User toUser(NewUserDTO newUserDTO, @Context PasswordEncoder passwordEncoder);

    @AfterMapping
    default void completeUserMapping(@MappingTarget User user, @Context PasswordEncoder passwordEncoder, NewUserDTO newUserDTO) {
        user.setPasswordHash(passwordEncoder.encode(newUserDTO.getPassword()));
        user.setPasswordHash(passwordEncoder.encode(newUserDTO.getPassword()));
    }

}
