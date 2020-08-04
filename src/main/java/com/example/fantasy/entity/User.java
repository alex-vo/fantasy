package com.example.fantasy.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
    @NotBlank
    String email;
    @Column(nullable = false)
    @NotBlank
    String passwordHash;
    @Column(nullable = false)
    Boolean blocked;
    @Column(nullable = false)
    @Min(0)
    Integer failedLoginAttempts;
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    Team team;
}
