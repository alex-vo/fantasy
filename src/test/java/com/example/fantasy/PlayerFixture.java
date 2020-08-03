package com.example.fantasy;

import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.PlayerPosition;
import com.example.fantasy.entity.Team;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PlayerFixture {

    public static Player preparePlayer(Team team, String firstName, String lastName, String country, LocalDate dateOfBirth,
                                       BigDecimal value, PlayerPosition position, Boolean isOnTransfer, BigDecimal transferPrice) {
        Player player = new Player();
        player.setTeam(team);
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setCountry(country);
        player.setDateOfBirth(dateOfBirth);
        player.setValue(value);
        player.setIsOnTransfer(isOnTransfer);
        player.setTransferPrice(transferPrice);
        player.setPosition(position);
        return player;
    }

    public static Player preparePlayer(Team team, String firstName, String lastName, String country, LocalDate dateOfBirth,
                                       BigDecimal value, PlayerPosition position) {
        return preparePlayer(team, firstName, lastName, country, dateOfBirth, value, position, false, null);
    }

}
