package com.example.fantasy.mapper;

import com.example.fantasy.dto.PlayerDTO;
import com.example.fantasy.dto.TeamDTO;
import com.example.fantasy.model.PlayerModel;
import com.example.fantasy.model.TeamModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring")
public interface TeamDTOMapper {

    TeamDTO toTeamDTO(TeamModel teamModel);

    PlayerDTO toPlayerDTO(PlayerModel playerModel);

    @AfterMapping
    default void completePlayerDTOMapping(@MappingTarget PlayerDTO playerDTO, PlayerModel playerModel) {
        playerDTO.setAge(Period.between(playerModel.getDateOfBirth(), LocalDate.now()).getYears());
    }

    @AfterMapping
    default void completeTeamDTOMapping(@MappingTarget TeamDTO teamDTO, TeamModel teamModel) {
        teamModel.getPlayers().stream()
                .map(PlayerModel::getValue)
                .reduce(BigDecimal::add)
                .ifPresent(teamDTO::setValue);
    }

}
