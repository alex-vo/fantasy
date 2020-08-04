package com.example.fantasy;

import com.example.fantasy.model.PlayerModel;
import com.example.fantasy.model.TeamModel;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class TestTeamModel implements TeamModel {
    Long id;
    String name;
    String country;
    BigDecimal balance;
    List<PlayerModel> players;
}
