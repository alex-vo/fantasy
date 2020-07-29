package com.example.fantasy.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "team")
public class Player {
    @Id
    @GeneratedValue
    Long id;
    @Column(nullable = false)
    String firstName;
    @Column(nullable = false)
    String lastName;
    @Column(nullable = false)
    String country;
    @Column(nullable = false)
    LocalDate dateOfBirth;
    @Column(nullable = false)
    BigDecimal value;
    @ManyToOne(optional = false)
    Team team;
    @Column(nullable = false)
    Boolean isOnTransfer;
    BigDecimal transferPrice;
    @Column(nullable = false)
    PlayerPosition position;

    public void setTeam(Team team) {
        this.team = team;
        team.getPlayers().add(this);
    }
}
