package com.example.fantasy.model;

import com.example.fantasy.entity.PlayerPosition;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PlayerModel {
    Long getId();

    String getFirstName();

    String getLastName();

    String getCountry();

    LocalDate getDateOfBirth();

    BigDecimal getValue();

    Boolean getIsOnTransfer();

    PlayerPosition getPosition();
}
