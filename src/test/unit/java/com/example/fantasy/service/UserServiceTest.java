package com.example.fantasy.service;

import com.example.fantasy.config.JwtTokenUtil;
import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.dto.UserDTO;
import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.Role;
import com.example.fantasy.entity.User;
import com.example.fantasy.mapper.UserMapper;
import com.example.fantasy.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Spy
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Spy
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    UserService userService;

    @Test
    public void shouldFailToLogin() {
        when(userRepository.findNonBlockedByEmail("abc@abc.com")).thenReturn(Optional.empty());

        try {
            userService.login(prepareUserDTO("abc@abc.com", "Qwerty123"));
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.BAD_REQUEST));
            assertThat(e.getReason(), is("User not found"));
        }
    }

    @Test
    public void shouldFailToLoginDueToWrongPassword() {
        doReturn(Optional.of(prepareUser(4L, 1, "hash123"))).when(userRepository).findNonBlockedByEmail("abc@abc.com");
        when(passwordEncoder.matches("Qwerty123", "hash123")).thenReturn(false);

        try {
            userService.login(prepareUserDTO("abc@abc.com", "Qwerty123"));
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.BAD_REQUEST));
            assertThat(e.getReason(), is("Wrong password"));
        }
        verify(userRepository).updateFailedLoginAttemptsCount(4L, 2);
    }

    @Test
    public void shouldLoginAndResetFailedLoginAttemptsCount() {
        doReturn(Optional.of(prepareUser(4L, 1, "hash123"))).when(userRepository).findNonBlockedByEmail("abc@abc.com");
        when(passwordEncoder.matches("Qwerty123", "hash123")).thenReturn(true);

        userService.login(prepareUserDTO("abc@abc.com", "Qwerty123"));

        verify(userRepository).updateFailedLoginAttemptsCount(4L, 0);
    }

    @Test
    public void shouldLogin() {
        doReturn(Optional.of(prepareUser(4L, 0, "hash123"))).when(userRepository).findNonBlockedByEmail("abc@abc.com");
        when(passwordEncoder.matches("Qwerty123", "hash123")).thenReturn(true);

        userService.login(prepareUserDTO("abc@abc.com", "Qwerty123"));

        verify(userRepository, never()).updateFailedLoginAttemptsCount(eq(4L), anyInt());
    }

    @Test
    public void shouldCreateUser() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("ab@ab.com");
        newUserDTO.setPassword("123456");
        newUserDTO.setTeamName("Liverpool");
        newUserDTO.setCountry("England");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.createUser(newUserDTO);

        verify(userRepository).save(userCaptor.capture());
        User user = userCaptor.getValue();
        List<Player> players = user.getTeam().getPlayers();
        assertThat(players, hasSize(20));
        assertThat(players.stream()
                .allMatch(player -> UserService.PLAYER_INITIAL_VALUE.equals(player.getValue())), is(true));
        UserService.TEAM_CONFIG.forEach((position, count) -> assertThat((int) players.stream()
                .filter(player -> position.equals(player.getPosition()))
                .count(), is(count)));
        assertThat(user, allOf(
                hasProperty("role", is(Role.ROLE_USER)),
                hasProperty("email", is("ab@ab.com")),
                hasProperty("passwordHash", is(notNullValue())),
                hasProperty("blocked", is(false)),
                hasProperty("failedLoginAttempts", is(0))
        ));
    }

    private User prepareUser(Long id, Integer failedLoginAttempts, String passwordHash) {
        User user = mock(User.class);
        when(user.getId()).thenReturn(id);
        when(user.getFailedLoginAttempts()).thenReturn(failedLoginAttempts);
        when(user.getPasswordHash()).thenReturn(passwordHash);
        return user;
    }


    private UserDTO prepareUserDTO(String email, String password) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        return userDTO;
    }
}
