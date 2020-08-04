package com.example.fantasy.service;

import com.example.fantasy.TestTeamModel;
import com.example.fantasy.dto.TeamDTO;
import com.example.fantasy.dto.UpdateTeamDTO;
import com.example.fantasy.mapper.TeamDTOMapper;
import com.example.fantasy.repository.user.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;
    @Spy
    TeamDTOMapper teamDTOMapper = Mappers.getMapper(TeamDTOMapper.class);

    @InjectMocks
    TeamService teamService;

    @Test
    public void shouldFailToGetTeamInfo() {
        when(teamRepository.findByOwnerId(290L)).thenReturn(Optional.empty());

        try {
            teamService.getTeamInfo(290L);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.NOT_FOUND));
        }
    }

    @Test
    public void shouldGetTeamInfo() {
        when(teamRepository.findByOwnerId(329L)).thenReturn(Optional.of(TestTeamModel.builder()
                .id(329L).players(List.of()).build()));

        TeamDTO result = teamService.getTeamInfo(329L);

        assertThat(result.getId(), is(329L));
    }

    @Test
    public void shouldFailToUpdateTeam() {
        when(teamRepository.updateTeamInformation(4L, 4L, "Juventus", "Italy")).thenReturn(0);
        UpdateTeamDTO updateTeamDTO = prepareUpdateTeamDTO("Juventus", "Italy");

        try {
            teamService.updateTeam(4L, 4L, updateTeamDTO);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus(), is(HttpStatus.BAD_REQUEST));
            assertThat(e.getReason(), is("failed to update team information"));
        }
    }

    @Test
    public void shouldUpdateTeam() {
        when(teamRepository.updateTeamInformation(4L, 4L, "Juventus", "Italy")).thenReturn(1);
        UpdateTeamDTO updateTeamDTO = prepareUpdateTeamDTO("Juventus", "Italy");

        teamService.updateTeam(4L, 4L, updateTeamDTO);
    }

    private UpdateTeamDTO prepareUpdateTeamDTO(String name, String country) {
        UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO();
        updateTeamDTO.setName(name);
        updateTeamDTO.setCountry(country);
        return updateTeamDTO;
    }

}
