package com.example.fantasy.dto;

import com.example.fantasy.entity.PlayerPosition;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerDTO {
    Long id;
    String firstName;
    String lastName;
    String country;
    LocalDate dateOfBirth;
    BigDecimal value;
    Boolean isOnTransfer;
    PlayerPosition position;
}
