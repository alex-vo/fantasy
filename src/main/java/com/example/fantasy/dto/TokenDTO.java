package com.example.fantasy.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenDTO {
    String accessToken;
    String refreshToken;
}
