package com.example.fantasy.service;

import com.example.fantasy.dto.TeamDTO;
import com.example.fantasy.exception.NotFoundException;
import com.example.fantasy.mapper.TeamDTOMapper;
import com.example.fantasy.model.TeamModel;
import com.example.fantasy.repository.admin.TeamForAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamForAdminRepository teamForAdminRepository;
    private final TeamDTOMapper teamDTOMapper;

    public TeamDTO getTeamInfo(Long userId) {
        TeamModel team = teamForAdminRepository.findByOwnerId(userId)
                .orElseThrow(NotFoundException::new);
        return teamDTOMapper.toTeamDTO(team);
    }

}
