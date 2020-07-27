package com.example.fantasy.model;

import java.util.List;

public interface TeamModel {
    Long getId();

    String getName();

    String getCountry();

    List<PlayerModel> getPlayers();
}
