package com.example.fantasy.repository.user;

import com.example.fantasy.PlayerFixture;
import com.example.fantasy.TeamFixture;
import com.example.fantasy.UserFixture;
import com.example.fantasy.entity.PlayerPosition;
import com.example.fantasy.entity.Team;
import com.example.fantasy.entity.User;
import com.example.fantasy.model.TeamModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamRepository teamRepository;

    User owner;

    @BeforeEach
    public void setUp() {
        owner = UserFixture.prepareUser("abc@abc.com");
        Team team = TeamFixture.prepareTeam("MU", "England", BigDecimal.valueOf(5_000_000));
        PlayerFixture.preparePlayer(team, "John", "Doe", "England", LocalDate.of(2000, 1, 1), BigDecimal.valueOf(1000), PlayerPosition.DEFENDER);
        PlayerFixture.preparePlayer(team, "John1", "Doe1", "England", LocalDate.of(2001, 1, 1), BigDecimal.valueOf(2000), PlayerPosition.ATTACKER);
        owner.setTeam(team);
        team.setOwner(owner);
        owner = userRepository.save(owner);
    }

    @Test
    public void shouldFindTeamInformationByOwnerId() {
        TeamModel result = teamRepository.findByOwnerId(owner.getId()).orElseThrow();

        assertThat(result, allOf(
                hasProperty("name", equalTo("MU")),
                hasProperty("country", equalTo("England")),
                hasProperty("balance", equalTo(BigDecimal.valueOf(5_000_000))),
                hasProperty("players", contains(
                        allOf(
                                hasProperty("firstName", equalTo("John")),
                                hasProperty("lastName", equalTo("Doe")),
                                hasProperty("country", equalTo("England")),
                                hasProperty("dateOfBirth", equalTo(LocalDate.of(2000, 1, 1))),
                                hasProperty("value", equalTo(BigDecimal.valueOf(1000))),
                                hasProperty("isOnTransfer", equalTo(false)),
                                hasProperty("transferPrice", is(nullValue())),
                                hasProperty("position", equalTo(PlayerPosition.DEFENDER))
                        ),
                        allOf(
                                hasProperty("firstName", equalTo("John1")),
                                hasProperty("lastName", equalTo("Doe1")),
                                hasProperty("country", equalTo("England")),
                                hasProperty("dateOfBirth", equalTo(LocalDate.of(2001, 1, 1))),
                                hasProperty("value", equalTo(BigDecimal.valueOf(2000))),
                                hasProperty("isOnTransfer", equalTo(false)),
                                hasProperty("transferPrice", is(nullValue())),
                                hasProperty("position", equalTo(PlayerPosition.ATTACKER))
                        )
                ))
        ));
    }

    @Test
    public void shouldUpdateTeamInformation() {
        int updatedRows = teamRepository.updateTeamInformation(owner.getId(), "Real Madrid", "Spain");

        assertThat(updatedRows, is(1));
        assertThat(teamRepository.findById(owner.getTeam().getId()).orElseThrow(), allOf(
                hasProperty("name", equalTo("Real Madrid")),
                hasProperty("country", equalTo("Spain"))
        ));
    }

    @Test
    public void shouldFailToUpdateOtherTeamInformation() {
        int updatedRows = teamRepository.updateTeamInformation(owner.getId() + 1, "Real Madrid", "Spain");

        assertThat(updatedRows, is(0));
    }

    @Test
    public void shouldUpdateBalance() {
        int updatedRows = teamRepository.updateBalance(owner.getTeam().getId(), BigDecimal.valueOf(150));

        assertThat(updatedRows, is(1));
        assertThat(teamRepository.findById(owner.getTeam().getId()).orElseThrow(),
                hasProperty("balance", comparesEqualTo(BigDecimal.valueOf(150))));
    }


}
