package com.example.fantasy.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email")
    String email;
    @NotBlank(message = "Password cannot be blank")
    @Length(min = 5, message = "Password must be at least 5 chars long")
    String password;
}
