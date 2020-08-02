package com.example.fantasy.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "team")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    Long id;
    @Column(nullable = false)
    Role role;
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false)
    String passwordHash;
    @Column(nullable = false)
    Boolean blocked;
    @OneToOne(cascade = CascadeType.ALL)
    Team team;

    public void setTeam(Team team) {
        this.team = team;
        if (team != null) {
            team.setOwner(this);
        }
    }
}
