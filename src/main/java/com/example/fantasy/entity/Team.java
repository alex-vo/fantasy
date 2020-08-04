package com.example.fantasy.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = "owner")
@ToString(exclude = "owner")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_seq")
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String country;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    List<Player> players;
    @OneToOne
    User owner;
    @Column(nullable = false)
    @Min(0)
    BigDecimal balance;

    public void addPlayer(Player p) {
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(p);
    }
}
