package com.example.fantasy;

import com.example.fantasy.entity.Team;

import java.math.BigDecimal;

public class TeamFixture {

    public static Team prepareTeam(String name, String country, BigDecimal balance) {
        Team team = new Team();
        team.setName(name);
        team.setCountry(country);
        team.setBalance(balance);
        return team;
    }

}
