package com.example.fantasy.controller.user;

import com.example.fantasy.config.FantasyAuthToken;
import com.example.fantasy.dto.TeamDTO;
import com.example.fantasy.dto.UpdateTeamDTO;
import com.example.fantasy.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/v1/team")
    public TeamDTO getTeamInfo(FantasyAuthToken auth) {
        return teamService.getTeamInfo(auth.getUserId());
    }

    @PutMapping("/v1/team/{teamId}")
    public void updateTeamInfo(FantasyAuthToken auth,
                               @PathVariable Long teamId,
                               @Valid @RequestBody UpdateTeamDTO updateTeamDTO) {
        teamService.updateTeam(auth.getUserId(), teamId, updateTeamDTO);
    }

}
