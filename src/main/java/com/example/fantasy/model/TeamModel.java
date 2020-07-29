package com.example.fantasy.model;

import java.math.BigDecimal;
import java.util.List;

public interface TeamModel {
    Long getId();

    String getName();

    String getCountry();

    BigDecimal getBalance();

    List<PlayerModel> getPlayers();
}
