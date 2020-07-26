package com.example.fantasy.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player {
    @Id
    @GeneratedValue
    Long id;
    @Column(nullable = false)
    BigDecimal value;
    @Column(nullable = false)
    Team team;
    @Column(nullable = false)
    Boolean isOnTransfer;
    @Column(nullable = false)
    PlayerPosition position;
}
