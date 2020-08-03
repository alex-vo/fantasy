package com.example.fantasy.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferPlacementDTO {
    @NotNull(message = "Price cannot be empty")
    @Min(value = 1, message = "Price must be positive")
    BigDecimal price;
}
