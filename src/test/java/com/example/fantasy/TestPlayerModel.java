package com.example.fantasy;

import com.example.fantasy.entity.PlayerPosition;
import com.example.fantasy.model.PlayerModel;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
public class TestPlayerModel implements PlayerModel {
    Long id;
    String firstName;
    String lastName;
    String country;
    LocalDate dateOfBirth;
    BigDecimal value;
    Boolean isOnTransfer;
    BigDecimal transferPrice;
    PlayerPosition position;
}
