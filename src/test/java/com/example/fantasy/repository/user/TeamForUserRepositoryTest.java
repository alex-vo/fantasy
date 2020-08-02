package com.example.fantasy.repository.user;

import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.PlayerPosition;
import com.example.fantasy.entity.Team;
import com.example.fantasy.entity.User;
import com.example.fantasy.model.TeamModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class TeamForUserRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testRepo() {
        User owner = prepareUser("asdasd@asdasd.lv", "2$B$asdkamsdkasmdka23423ojmk23j4n2m3");

        Team team = prepareTeam("MU", "England", owner);
        team.setPlayers(List.of(
                preparePlayer(team, "John", "Doe", "England", LocalDate.of(2000, 1, 1), BigDecimal.valueOf(1000), PlayerPosition.DEFENDER),
                preparePlayer(team, "John1", "Doe1", "England", LocalDate.of(2001, 1, 1), BigDecimal.valueOf(2000), PlayerPosition.ATTACKER)
        ));
        teamRepository.save(team);
        Long ownerId = teamRepository.findAll().iterator().next().getOwner().getId();

        TeamModel teamModel = teamRepository.findByOwnerId(ownerId).orElseThrow();
        assert teamModel.getName().equals("MU");
        assert teamModel.getPlayers().size() == 2;
        assert teamModel.getPlayers().get(0).getFirstName().equals("John");
        assert teamModel.getPlayers().get(1).getFirstName().equals("John1");
    }

    private Team prepareTeam(String name, String country, User owner) {
        Team team = new Team();
        team.setName(name);
        team.setCountry(country);
        team.setOwner(owner);
        return team;
    }

    private User prepareUser(String email, String passwordHash) {
        User owner = new User();
        owner.setEmail(email);
        owner.setPasswordHash(passwordHash);
        owner.setBlocked(false);
        return owner;
    }

    private Player preparePlayer(Team team, String firstName, String lastName, String country, LocalDate dateOfBirth,
                                 BigDecimal value, PlayerPosition position) {
        Player player = new Player();
        player.setTeam(team);
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setCountry(country);
        player.setDateOfBirth(dateOfBirth);
        player.setValue(value);
        player.setIsOnTransfer(false);
        player.setPosition(position);
        return player;
    }

}
