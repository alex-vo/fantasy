package com.example.fantasy.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "team")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_seq")
    Long id;
    @Column(nullable = false)
    @NotBlank
    String firstName;
    @Column(nullable = false)
    @NotBlank
    String lastName;
    @Column(nullable = false)
    @NotBlank
    String country;
    @Column(nullable = false)
    LocalDate dateOfBirth;
    @Column(nullable = false)
    @Min(1)
    BigDecimal value;
    @ManyToOne/*(optional = false)*/
    Team team;
    @Column(nullable = false)
    Boolean isOnTransfer;
    @Min(1)
    BigDecimal transferPrice;
    @Column(nullable = false)
    PlayerPosition position;

    public void setTeam(Team team) {
        this.team = team;
        team.addPlayer(this);
    }
}
