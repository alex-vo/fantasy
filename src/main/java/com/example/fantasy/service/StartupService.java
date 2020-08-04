package com.example.fantasy.service;

import com.example.fantasy.entity.*;
import com.example.fantasy.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class StartupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public StartupService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          @Value("${admin.username}") String adminUsername,
                          @Value("${admin.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @EventListener
    public void appReady(ApplicationStartedEvent e) {
        if (userRepository.findNonBlockedByEmail(adminUsername).isPresent()) {
            return;
        }
        userRepository.save(prepareUser(adminUsername, adminPassword, Role.ROLE_ADMIN));
//        User user = prepareUser("aa@bb.lv", "12345", Role.ROLE_USER);
//        Team team = prepareTeam("MU", "England");
//        user.setTeam(team);
//        preparePlayer(team, "John", "Zz", "England", LocalDate.of(2000, 1, 1), BigDecimal.valueOf(1000), PlayerPosition.DEFENDER);
//        preparePlayer(team, "John1", "Aa", "Wales", LocalDate.of(2001, 1, 1), BigDecimal.valueOf(2000), PlayerPosition.ATTACKER);
//        preparePlayer(team, "Stan", "Bb", "Wales", LocalDate.of(2001, 1, 1), BigDecimal.valueOf(1995), PlayerPosition.GOALKEEPER);
//        userRepository.save(user);
    }

    private Team prepareTeam(String name, String country/*, User owner*/) {
        Team team = new Team();
        team.setName(name);
        team.setCountry(country);
        team.setBalance(BigDecimal.valueOf(5_000_000));
        return team;
    }

    private User prepareUser(String email, String password, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setBlocked(false);
        user.setFailedLoginAttempts(0);
        user.setRole(role);
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
        player.setIsOnTransfer(false);
        player.setPosition(position);
        player.setTeam(team);
        return player;
    }
}
