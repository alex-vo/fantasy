package com.example.fantasy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateTeamDTO {
    @NotBlank(message = "Team name cannot be blank")
    String name;
    @NotBlank(message = "Country cannot be blank")
    String country;
}
