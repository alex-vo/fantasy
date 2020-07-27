package com.example.fantasy.mapper;

import com.example.fantasy.dto.PlayerForUserDTO;
import com.example.fantasy.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    void map(@MappingTarget Player player, PlayerForUserDTO playerForUserDTO);

}
