package com.example.fantasy.service;

import com.example.fantasy.TestPlayerModel;
import com.example.fantasy.dto.PlayerDTO;
import com.example.fantasy.dto.UpdatePlayerDTO;
import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.Team;
import com.example.fantasy.entity.User;
import com.example.fantasy.mapper.TeamDTOMapper;
import com.example.fantasy.repository.user.PlayerRepository;
import com.example.fantasy.repository.user.TeamRepository;
import com.example.fantasy.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TeamRepository teamRepository;
    @Spy
    TeamDTOMapper teamDTOMapper = Mappers.getMapper(TeamDTOMapper.class);

    @InjectMocks
    PlayerService playerService;

    @Test
    public void shouldGetPlayer() {
        when(playerRepository.findPlayerById(159L))
                .thenReturn(Optional.of(TestPlayerModel.builder().id(159L).dateOfBirth(LocalDate.of(1995, 5, 5)).build()));

        PlayerDTO result = playerService.getPlayer(159L);

        assertThat(result, hasProperty("id", is(159L)));
    }

    @Test
    public void shouldFailToGetPlayer() {
        when(playerRepository.findPlayerById(159L)).thenReturn(Optional.empty());

        try {
            playerService.getPlayer(159L);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.NOT_FOUND));
        }
    }

    @Test
    public void shouldListPlayersOnTransfer() {
        when(playerRepository.findPlayersOnTransfer(eq("England"), eq("Sheffield"), eq("Wickham"), eq(BigDecimal.valueOf(1)),
                eq(BigDecimal.valueOf(2)), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(
                TestPlayerModel.builder().id(159L).dateOfBirth(LocalDate.of(1995, 5, 5)).build(),
                TestPlayerModel.builder().id(159L).dateOfBirth(LocalDate.of(1995, 5, 5)).build(),
                TestPlayerModel.builder().id(159L).dateOfBirth(LocalDate.of(1995, 5, 5)).build()
        )));

        Page<PlayerDTO> result = playerService.listPlayersOnTransfer("England", "Sheffield", "Wickham", BigDecimal.valueOf(1),
                BigDecimal.valueOf(2), PageRequest.of(0, 5));

        assertThat(result.getContent(), hasSize(3));
    }

    @Test
    public void shouldUpdatePlayer() {
        when(playerRepository.updatePlayerInformation(1L, 1L, "Connor", "Wickham", "England")).thenReturn(1);

        playerService.updatePlayer(1L, 1L, prepareUpdatePlayerDTO("Connor", "Wickham", "England"));
    }

    @Test
    public void shouldFailUpdatePlayer() {
        when(playerRepository.updatePlayerInformation(1L, 1L, "Connor", "Wickham", "England")).thenReturn(0);

        try {
            playerService.updatePlayer(1L, 1L, prepareUpdatePlayerDTO("Connor", "Wickham", "England"));
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.BAD_REQUEST));
            assertThat(e.getReason(), is("failed to update player information"));
        }
    }

    @Test
    public void shouldPlacePlayerOnTransfer() {
        when(playerRepository.placePlayerOnTransfer(2L, 2L, BigDecimal.TEN)).thenReturn(1);

        playerService.placePlayerOnTransfer(2L, 2L, BigDecimal.TEN);
    }

    @Test
    public void shouldFailToPlacePlayerOnTransfer() {
        when(playerRepository.placePlayerOnTransfer(2L, 2L, BigDecimal.TEN)).thenReturn(1);

        try {
            playerService.placePlayerOnTransfer(2L, 2L, BigDecimal.TEN);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.BAD_REQUEST));
            assertThat(e.getReason(), is("failed to place player on transfer"));
        }
    }

    @Test
    public void shouldFailToBuyPlayerIfBuyerNotFound() {
        when(userRepository.findById(34L)).thenReturn(Optional.empty());

        try {
            playerService.buyPlayer(34L, 2312L);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.NOT_FOUND));
        }
    }

    @Test
    public void shouldFailToBuyPlayerIfPlayerNotFound() {
        when(userRepository.findById(34L)).thenReturn(Optional.of(mock(User.class)));
        when(playerRepository.findPlayerOnTransferById(2312L)).thenReturn(Optional.empty());

        try {
            playerService.buyPlayer(34L, 2312L);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.NOT_FOUND));
        }
    }

    @Test
    public void shouldFailToBuyPlayerIfPlayerIsInThisTeamAlready() {
        Team team = mock(Team.class);
        doReturn(Optional.of(prepareUser(team))).when(userRepository).findById(34L);
        doReturn(Optional.of(preparePlayer(team))).when(playerRepository).findPlayerOnTransferById(2312L);

        try {
            playerService.buyPlayer(34L, 2312L);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.BAD_REQUEST));
            assertThat(e.getReason(), is("cannot buy players from own team"));
        }
    }

    @Test
    public void shouldFailToBuyPlayerIfNotEnoughFunds() {
        Team team = mock(Team.class);
        when(team.getBalance()).thenReturn(BigDecimal.ONE);
        doReturn(Optional.of(prepareUser(team))).when(userRepository).findById(34L);
        doReturn(Optional.of(preparePlayer(mock(Team.class), BigDecimal.valueOf(90), BigDecimal.TEN)))
                .when(playerRepository).findPlayerOnTransferById(2312L);

        try {
            playerService.buyPlayer(34L, 2312L);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.BAD_REQUEST));
            assertThat(e.getReason(), is("not enough funds"));
        }
    }

    @Test
    public void shouldBuyPlayer() {
        Team team = mock(Team.class);
        when(team.getId()).thenReturn(20L);
        when(team.getBalance()).thenReturn(BigDecimal.TEN);
        doReturn(Optional.of(prepareUser(team))).when(userRepository).findById(34L);
        Team otherTeam = mock(Team.class);
        when(otherTeam.getId()).thenReturn(42L);
        when(otherTeam.getBalance()).thenReturn(BigDecimal.valueOf(2));
        doReturn(Optional.of(preparePlayer(otherTeam, BigDecimal.valueOf(100), BigDecimal.ONE))).when(playerRepository)
                .findPlayerOnTransferById(2312L);

        playerService.buyPlayer(34L, 2312L);

        verify(teamRepository).updateBalance(42L, BigDecimal.valueOf(3));
        verify(teamRepository).updateBalance(20L, BigDecimal.valueOf(9));
        verify(playerRepository).performTransfer(eq(2312L), eq(team),
                argThat(argument -> BigDecimal.valueOf(109).compareTo(argument) < 0 && BigDecimal.valueOf(201).compareTo(argument) > 0)
        );
    }

    private User prepareUser(Team team) {
        User user = mock(User.class);
        when(user.getTeam()).thenReturn(team);
        return user;
    }

    private Player preparePlayer(Team team) {
        return preparePlayer(team, null, null);
    }

    private Player preparePlayer(Team team, BigDecimal value, BigDecimal transferPrice) {
        Player player = mock(Player.class);
        doReturn(team).when(player).getTeam();
        when(player.getValue()).thenReturn(value);
        when(player.getTransferPrice()).thenReturn(transferPrice);
        return player;
    }

    private UpdatePlayerDTO prepareUpdatePlayerDTO(String firstName, String lastName, String country) {
        UpdatePlayerDTO updatePlayerDTO = new UpdatePlayerDTO();
        updatePlayerDTO.setFirstName(firstName);
        updatePlayerDTO.setLastName(lastName);
        updatePlayerDTO.setCountry(country);
        return updatePlayerDTO;
    }

}
