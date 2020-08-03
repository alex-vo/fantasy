package com.example.fantasy.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePlayerDTO {
    @NotBlank(message = "First name cannot be blank")
    String firstName;
    @NotBlank(message = "Last name cannot be blank")
    String lastName;
    @NotBlank(message = "Country cannot be blank")
    String country;
}
