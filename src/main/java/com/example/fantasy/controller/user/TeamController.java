package com.example.fantasy.controller.user;

import com.example.fantasy.dto.TeamDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController {

    @GetMapping("/v1/team")
    public TeamDTO getTeamInfo() {
        return null;
    }

}
