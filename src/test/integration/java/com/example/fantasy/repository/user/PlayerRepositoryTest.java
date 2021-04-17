package com.example.fantasy.repository.user;

import com.example.fantasy.PlayerFixture;
import com.example.fantasy.TeamFixture;
import com.example.fantasy.UserFixture;
import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.PlayerPosition;
import com.example.fantasy.entity.Team;
import com.example.fantasy.entity.User;
import com.example.fantasy.model.PlayerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TransactionTemplate transactionTemplate;
    @Autowired
    EntityManager entityManager;

    Team team;
    User owner;

    @BeforeEach
    public void setUp() {
        team = setUpUserWithTeam("abc@abc.com", "MU", "England").getTeam();
    }

    private User setUpUserWithTeam(String email, String teamName, String country) {
        owner = UserFixture.prepareUser(email);
        Team team = TeamFixture.prepareTeam(teamName, country, BigDecimal.valueOf(5_000_000));
        owner.setTeam(team);
        team.setOwner(owner);
        owner = userRepository.save(owner);
        return owner;
    }

    @Test
    public void shouldFindPlayerById() {
        Player p = playerRepository.save(PlayerFixture.preparePlayer(team, "Fabien", "Leroy", "France", LocalDate.of(1990, 10, 10),
                BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123)));

        PlayerModel result = playerRepository.findPlayerById(p.getId()).orElseThrow();

        assertThat(result, allOf(
                hasProperty("firstName", equalTo("Fabien")),
                hasProperty("lastName", equalTo("Leroy")),
                hasProperty("country", equalTo("France")),
                hasProperty("dateOfBirth", equalTo(LocalDate.of(1990, 10, 10))),
                hasProperty("value", equalTo(BigDecimal.valueOf(3000))),
                hasProperty("isOnTransfer", equalTo(true)),
                hasProperty("transferPrice", comparesEqualTo(BigDecimal.valueOf(123))),
                hasProperty("position", equalTo(PlayerPosition.GOALKEEPER))
        ));
    }

    @Test
    public void shouldFindOnePlayerOnTransfer() {
        prepareManyDifferentPlayers();

        Page<PlayerModel> result = playerRepository.findPlayersOnTransfer("France", team.getName(), "A", BigDecimal.valueOf(2999),
                BigDecimal.valueOf(3001), PageRequest.of(0, 5));

        assertThat(result.getTotalElements(), is(1L));
        assertThat(result.getContent(), contains(
                allOf(
                        hasProperty("firstName", equalTo("A")),
                        hasProperty("lastName", equalTo("A")),
                        hasProperty("country", equalTo("France")),
                        hasProperty("value", equalTo(BigDecimal.valueOf(3000))),
                        hasProperty("isOnTransfer", is(true))
                )
        ));
    }

    @Test
    public void shouldFindManyPlayersOnTransfer() {
        prepareManyDifferentPlayers();

        Page<PlayerModel> result = playerRepository.findPlayersOnTransfer(null, null, null, null,
                null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements(), is(6L));
    }

    @Test
    public void shouldUpdatePlayerInformation() {
        Player p = playerRepository.save(PlayerFixture.preparePlayer(team, "Fabien", "Leroy", "France", LocalDate.of(1990, 10, 10),
                BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123)));

        int result = playerRepository.updatePlayerInformation(p.getId(), owner.getId(), "John", "Doe", "England");
        entityManager.clear();

        assertThat(result, is(1));
        assertThat(playerRepository.findById(p.getId()).orElseThrow(), allOf(
                hasProperty("firstName", equalTo("John")),
                hasProperty("lastName", equalTo("Doe")),
                hasProperty("country", equalTo("England"))
        ));
    }

    @Test
    public void shouldPlacePlayerOnTransfer() {
        Player p = playerRepository.save(PlayerFixture.preparePlayer(team, "Fabien", "Leroy", "France", LocalDate.of(1990, 10, 10),
                BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, false, null));

        int result = playerRepository.placePlayerOnTransfer(p.getId(), owner.getId(), BigDecimal.valueOf(111));
        entityManager.clear();

        assertThat(result, is(1));
        assertThat(playerRepository.findById(p.getId()).orElseThrow(), allOf(
                hasProperty("isOnTransfer", is(true)),
                hasProperty("transferPrice", comparesEqualTo(BigDecimal.valueOf(111)))
        ));
    }

    @Test
    public void shouldFailToPlacePlayerOnTransferIfAlreadyOnTransfer() {
        Player p = playerRepository.save(PlayerFixture.preparePlayer(team, "Fabien", "Leroy", "France", LocalDate.of(1990, 10, 10),
                BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123)));

        int result = playerRepository.placePlayerOnTransfer(p.getId(), owner.getId(), BigDecimal.valueOf(111));

        assertThat(result, is(0));
    }

    @Test
    public void shouldFindPlayerOnTransferById() {
        Player p = playerRepository.save(PlayerFixture.preparePlayer(team, "Fabien", "Leroy", "France", LocalDate.of(1990, 10, 10),
                BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123)));

        assertThat(playerRepository.findPlayerOnTransferById(p.getId()).isPresent(), is(true));
    }

    @Test
    public void shouldFailToFindPlayerOnTransferById() {
        Player p = playerRepository.save(PlayerFixture.preparePlayer(team, "Fabien", "Leroy", "France", LocalDate.of(1990, 10, 10),
                BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, false, null));

        assertThat(playerRepository.findPlayerOnTransferById(p.getId()).isPresent(), is(false));
    }

    @Test
    public void shouldPerformTransfer() {
        Team anotherTeam = setUpUserWithTeam("abc1@abc.com", "Roma", "Italy").getTeam();
        Player p = playerRepository.save(PlayerFixture.preparePlayer(team, "Fabien", "Leroy", "France", LocalDate.of(1990, 10, 10),
                BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(1_000_000)));

        playerRepository.performTransfer(p.getId(), anotherTeam, BigDecimal.valueOf(3100));
        entityManager.clear();

        assertThat(playerRepository.findById(p.getId()).orElseThrow(), allOf(
                hasProperty("team", allOf(
                        hasProperty("name", is("Roma"))
                )),
                hasProperty("isOnTransfer", is(false)),
                hasProperty("transferPrice", is(nullValue())),
                hasProperty("value", comparesEqualTo(BigDecimal.valueOf(3100)))
        ));
    }

    private void prepareManyDifferentPlayers() {
        Team anotherTeam = setUpUserWithTeam("abc1@abc.com", "Roma", "Italy").getTeam();
        playerRepository.saveAll(List.of(
                PlayerFixture.preparePlayer(team, "A", "A", "France", LocalDate.of(1990, 10, 10),
                        BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123)),
                PlayerFixture.preparePlayer(team, "B", "B", "Italy", LocalDate.of(1990, 10, 10),
                        BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123)),
                PlayerFixture.preparePlayer(anotherTeam, "C", "C", "Italy", LocalDate.of(1990, 10, 10),
                        BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123)),
                PlayerFixture.preparePlayer(team, "D", "D", "France", LocalDate.of(1990, 10, 10),
                        BigDecimal.valueOf(3000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123)),
                PlayerFixture.preparePlayer(team, "B", "A", "France", LocalDate.of(1990, 10, 10),
                        BigDecimal.valueOf(2000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123)),
                PlayerFixture.preparePlayer(team, "D", "A", "France", LocalDate.of(1990, 10, 10),
                        BigDecimal.valueOf(4000), PlayerPosition.GOALKEEPER, true, BigDecimal.valueOf(123))
        ));
    }

}
