package com.example.fantasy.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue
    Long id;
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false)
    String passwordHash;
    @Column(nullable = false)
    Boolean blocked;
    @OneToOne
    Team team;
}
