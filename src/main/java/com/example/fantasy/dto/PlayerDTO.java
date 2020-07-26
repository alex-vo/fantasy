package com.example.fantasy.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerDTO {
    String firstName;
    String lastName;
    String country;
    LocalDate dateOfBirth;
    BigDecimal value;
}
