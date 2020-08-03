package com.example.fantasy.mapper;

import com.example.fantasy.dto.TeamDTO;
import com.example.fantasy.entity.PlayerPosition;
import com.example.fantasy.model.PlayerModel;
import com.example.fantasy.model.TeamModel;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TeamDTOMapperTest {

    @Test
    public void testTeamDTOMapping() {
        TeamDTOMapper m = Mappers.getMapper(TeamDTOMapper.class);
        TestTeamModel teamModel = TestTeamModel.builder()
                .id(140L)
                .country("England")
                .name("Chelsea")
                .balance(BigDecimal.ONE)
                .players(List.of(
                        TestPlayerModel.builder().firstName("Diego").lastName("Maradona").country("Argentina")
                                .dateOfBirth(LocalDate.of(1960, 1, 1)).value(BigDecimal.valueOf(1_000_001))
                                .isOnTransfer(false).position(PlayerPosition.ATTACKER).build(),
                        TestPlayerModel.builder().firstName("Zinedine").lastName("Zidane").country("France")
                                .dateOfBirth(LocalDate.of(1968, 2, 2)).value(BigDecimal.valueOf(1_000_002))
                                .isOnTransfer(true).position(PlayerPosition.MIDFIELDER).build()
                ))
                .build();

        TeamDTO teamDTO = m.toTeamDTO(teamModel);

        //todo assertions
        System.out.println(teamDTO);
    }

    @Builder
    @Getter
    static class TestTeamModel implements TeamModel {
        Long id;
        String name;
        String country;
        BigDecimal balance;
        List<PlayerModel> players;
    }

    @Builder
    @Getter
    static class TestPlayerModel implements PlayerModel {
        Long id;
        String firstName;
        String lastName;
        String country;
        LocalDate dateOfBirth;
        BigDecimal value;
        Boolean isOnTransfer;
        BigDecimal transferPrice;
        PlayerPosition position;
    }

}
