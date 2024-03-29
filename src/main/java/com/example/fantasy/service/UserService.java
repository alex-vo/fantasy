package com.example.fantasy.service;

import com.example.fantasy.config.JwtTokenUtil;
import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.dto.TokenDTO;
import com.example.fantasy.dto.UserDTO;
import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.PlayerPosition;
import com.example.fantasy.entity.Team;
import com.example.fantasy.entity.User;
import com.example.fantasy.mapper.UserMapper;
import com.example.fantasy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final BigDecimal PLAYER_INITIAL_VALUE = BigDecimal.valueOf(1_000_000);
    public static final BigDecimal TEAM_INITIAL_BALANCE = BigDecimal.valueOf(5_000_000);
    public static final Map<PlayerPosition, Integer> TEAM_CONFIG = Map.of(
            PlayerPosition.GOALKEEPER, 3,
            PlayerPosition.DEFENDER, 6,
            PlayerPosition.MIDFIELDER, 6,
            PlayerPosition.ATTACKER, 5
    );

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public TokenDTO login(UserDTO userDTO) {
        User user = userRepository.findNonBlockedByEmail(userDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPasswordHash())) {
            userRepository.updateFailedLoginAttemptsCount(user.getId(), user.getFailedLoginAttempts() + 1);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
        } else if (user.getFailedLoginAttempts() > 0) {
            userRepository.updateFailedLoginAttemptsCount(user.getId(), 0);
        }

        return new TokenDTO(jwtTokenUtil.generateJwtToken(userDTO.getEmail()));
    }

    public void createUser(NewUserDTO newUserDTO) {
        User user = userMapper.toUser(newUserDTO, passwordEncoder);
        Team team = createInitialTeam(user, newUserDTO.getTeamName(), newUserDTO.getCountry());
        user.setTeam(team);
        userRepository.save(user);
    }

    private Team createInitialTeam(User owner, String teamName, String country) {
        Team team = new Team();
        team.setOwner(owner);
        team.setName(teamName);
        team.setCountry(country);
        team.setBalance(TEAM_INITIAL_BALANCE);
        long eighteenYearsAgo = LocalDate.now().minusYears(18).toEpochDay();
        long fortyYearsAgo = LocalDate.now().minusYears(40).toEpochDay();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        TEAM_CONFIG.forEach((position, amount) -> IntStream.range(0, amount)
                .forEach(v -> createPlayer(country, team, eighteenYearsAgo, fortyYearsAgo, threadLocalRandom, position)));
        return team;
    }

    private void createPlayer(String country, Team team, long eighteenYearsAgo, long fortyYearsAgo,
                              ThreadLocalRandom threadLocalRandom, PlayerPosition position) {
        Player p = new Player();
        p.setFirstName(RandomStringUtils.randomAlphabetic(5));
        p.setLastName(RandomStringUtils.randomAlphabetic(5));
        p.setCountry(country);
        long birthDateEpochDay = threadLocalRandom.nextLong(fortyYearsAgo, eighteenYearsAgo);
        p.setDateOfBirth(LocalDate.ofEpochDay(birthDateEpochDay));
        p.setValue(PLAYER_INITIAL_VALUE);
        p.setIsOnTransfer(false);
        p.setPosition(position);
        p.setTeam(team);
    }

}
