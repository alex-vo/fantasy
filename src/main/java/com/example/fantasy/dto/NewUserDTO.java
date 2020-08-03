package com.example.fantasy.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserDTO extends UserDTO {
    @NotBlank(message = "Team name cannot be blank")
    String teamName;
    @NotBlank(message = "Country cannot be blank")
    String country;
}

