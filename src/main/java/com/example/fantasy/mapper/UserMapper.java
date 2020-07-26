package com.example.fantasy.mapper;

import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.entity.Team;
import com.example.fantasy.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = {PasswordEncoder.class, Team.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "blocked", constant = "false")
    @Mapping(target = "passwordHash", expression = "java(passwordEncoder.encode(userDTO.getPassword()))")
    @Mapping(target = "team", ignore = true)
    User toUser(NewUserDTO newUserDTO);

    @AfterMapping
    default void completeUserMapping(@MappingTarget User user, NewUserDTO newUserDTO) {
        Team team = new Team();
        team.setName(newUserDTO.getTeamName());
        user.setTeam(team);
    }

}
