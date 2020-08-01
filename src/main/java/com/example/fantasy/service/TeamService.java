package com.example.fantasy.service;

import com.example.fantasy.dto.TeamDTO;
import com.example.fantasy.dto.UpdateTeamDTO;
import com.example.fantasy.exception.BadRequestException;
import com.example.fantasy.exception.NotFoundException;
import com.example.fantasy.mapper.TeamDTOMapper;
import com.example.fantasy.model.TeamModel;
import com.example.fantasy.repository.admin.SecuredTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final SecuredTeamRepository securedTeamRepository;
    private final TeamDTOMapper teamDTOMapper;

    public TeamDTO getTeamInfo(Long userId) {
        TeamModel team = securedTeamRepository.findByOwnerId(userId)
                .orElseThrow(NotFoundException::new);
        return teamDTOMapper.toTeamDTO(team);
    }

    public void updateTeam(Long ownerId, Long teamId, UpdateTeamDTO updateTeamDTO) {
        int updatedRows = securedTeamRepository.updateTeamInformation(teamId, ownerId, updateTeamDTO.getName(),
                updateTeamDTO.getCountry());
        if (updatedRows != 1) {
            throw new BadRequestException();
        }
    }

}
