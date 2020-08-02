package com.example.fantasy.service;

import com.example.fantasy.dto.TeamDTO;
import com.example.fantasy.dto.UpdateTeamDTO;
import com.example.fantasy.mapper.TeamDTOMapper;
import com.example.fantasy.model.TeamModel;
import com.example.fantasy.repository.user.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamDTOMapper teamDTOMapper;

    public TeamDTO getTeamInfo(Long userId) {
        TeamModel team = teamRepository.findByOwnerId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return teamDTOMapper.toTeamDTO(team);
    }

    public void updateTeam(Long ownerId, Long teamId, UpdateTeamDTO updateTeamDTO) {
        int updatedRows = teamRepository.updateTeamInformation(teamId, ownerId, updateTeamDTO.getName(),
                updateTeamDTO.getCountry());
        if (updatedRows != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "failed to update team information");
        }
    }

}
