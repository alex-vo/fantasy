package com.example.fantasy.dto;

import com.example.fantasy.entity.PlayerPosition;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerDTO {
    Long id;
    String firstName;
    String lastName;
    String country;
    Integer age;
    BigDecimal value;
    Boolean isOnTransfer;
    BigDecimal transferPrice;
    PlayerPosition position;
}
