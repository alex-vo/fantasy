package com.example.fantasy.startup;

import com.example.fantasy.entity.*;
import com.example.fantasy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StartupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void appReady(ApplicationStartedEvent e) {
        User user = prepareUser("aa@bb.lv", "123");

        Team team = prepareTeam("MU", "England");
        user.setTeam(team);
        preparePlayer(team, "John", "Zz", "England", LocalDate.of(2000, 1, 1), BigDecimal.valueOf(1000), PlayerPosition.DEFENDER);
        preparePlayer(team, "John1", "Aa", "Wales", LocalDate.of(2001, 1, 1), BigDecimal.valueOf(2000), PlayerPosition.ATTACKER);
        preparePlayer(team, "Stan", "Bb", "Wales", LocalDate.of(2001, 1, 1), BigDecimal.valueOf(1995), PlayerPosition.GOALKEEPER);
        userRepository.save(user);
    }

    private Team prepareTeam(String name, String country/*, User owner*/) {
        Team team = new Team();
        team.setName(name);
        team.setCountry(country);
        team.setBalance(BigDecimal.valueOf(5_000_000));
        return team;
    }

    private User prepareUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setBlocked(false);
        user.setRole(Role.ROLE_USER);
        return user;
    }

    private Player preparePlayer(Team team, String firstName, String lastName, String country, LocalDate dateOfBirth,
                                 BigDecimal value, PlayerPosition position) {
        Player player = new Player();
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setCountry(country);
        player.setDateOfBirth(dateOfBirth);
        player.setValue(value);
        player.setIsOnTransfer(true);
        player.setPosition(position);
        player.setTeam(team);
        return player;
    }
}
