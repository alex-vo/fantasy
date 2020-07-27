package com.example.fantasy.controller.user;

import com.example.fantasy.dto.TeamDTO;
import com.example.fantasy.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/v1/team")
    @PreAuthorize("isAnonymous()")
    public TeamDTO getTeamInfo() {
        return teamService.getTeamInfo(1L);
    }

}
