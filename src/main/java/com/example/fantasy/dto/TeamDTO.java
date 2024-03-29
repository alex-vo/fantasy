package com.example.fantasy.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeamDTO {
    Long id;
    String name;
    String country;
    BigDecimal value;
    BigDecimal balance;
    List<PlayerDTO> players;
}
