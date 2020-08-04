package com.example.fantasy.mapper;

import com.example.fantasy.TestPlayerModel;
import com.example.fantasy.TestTeamModel;
import com.example.fantasy.dto.TeamDTO;
import com.example.fantasy.entity.PlayerPosition;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TeamDTOMapperTest {

    @Test
    public void testTeamDTOMapping() {
        TeamDTOMapper mapper = Mappers.getMapper(TeamDTOMapper.class);
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
                                .isOnTransfer(true).transferPrice(BigDecimal.valueOf(68_000_000))
                                .position(PlayerPosition.MIDFIELDER).build()
                ))
                .build();

        TeamDTO teamDTO = mapper.toTeamDTO(teamModel);

        assertThat(teamDTO, allOf(
                hasProperty("id", is(140L)),
                hasProperty("country", is("England")),
                hasProperty("name", is("Chelsea")),
                hasProperty("balance", comparesEqualTo(BigDecimal.ONE)),
                hasProperty("players", contains(
                        allOf(
                                hasProperty("firstName", equalTo("Diego")),
                                hasProperty("lastName", equalTo("Maradona")),
                                hasProperty("country", equalTo("Argentina")),
                                hasProperty("age", equalTo(Period.between(LocalDate.of(1960, 1, 1), LocalDate.now()).getYears())),
                                hasProperty("value", equalTo(BigDecimal.valueOf(1_000_001))),
                                hasProperty("isOnTransfer", equalTo(false)),
                                hasProperty("transferPrice", is(nullValue())),
                                hasProperty("position", equalTo(PlayerPosition.ATTACKER))
                        ),
                        allOf(
                                hasProperty("firstName", equalTo("Zinedine")),
                                hasProperty("lastName", equalTo("Zidane")),
                                hasProperty("country", equalTo("France")),
                                hasProperty("age", equalTo(Period.between(LocalDate.of(1968, 2, 2), LocalDate.now()).getYears())),
                                hasProperty("value", equalTo(BigDecimal.valueOf(1_000_002))),
                                hasProperty("isOnTransfer", equalTo(true)),
                                hasProperty("transferPrice", comparesEqualTo(BigDecimal.valueOf(68_000_000))),
                                hasProperty("position", equalTo(PlayerPosition.MIDFIELDER))
                        )
                ))
        ));
    }

}
