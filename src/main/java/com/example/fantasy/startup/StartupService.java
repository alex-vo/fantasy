package com.example.fantasy.startup;

import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.PlayerPosition;
import com.example.fantasy.entity.Team;
import com.example.fantasy.entity.User;
import com.example.fantasy.repository.admin.TeamForAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StartupService {

    private final TeamForAdminRepository teamForAdminRepository;

    @EventListener
    public void appReady(ApplicationStartedEvent e) {
        User owner = prepareUser("asdasd@asdasd.lv", "2$B$asdkamsdkasmdka23423ojmk23j4n2m3");

        Team team = prepareTeam("MU", "England", owner);
        preparePlayer(team, "John", "Doe", "England", LocalDate.of(2000, 1, 1), BigDecimal.valueOf(1000), PlayerPosition.DEFENDER);
        preparePlayer(team, "John1", "Doe1", "England", LocalDate.of(2001, 1, 1), BigDecimal.valueOf(2000), PlayerPosition.ATTACKER);
        teamForAdminRepository.save(team);
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
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setCountry(country);
        player.setDateOfBirth(dateOfBirth);
        player.setValue(value);
        player.setIsOnTransfer(false);
        player.setPosition(position);
        player.setTeam(team);
        return player;
    }

}
