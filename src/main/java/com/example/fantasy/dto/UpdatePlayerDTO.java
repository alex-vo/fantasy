package com.example.fantasy.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePlayerDTO {
    String firstName;
    String lastName;
    String country;
}
