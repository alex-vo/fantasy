package com.example.fantasy.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
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
    @GeneratedValue
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String country;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    List<Player> players = new ArrayList<>();
    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    User owner;
    @Column(nullable = false)
    BigDecimal balance;

//    public void setOwner(User owner) {
//        this.owner = owner;
//        if (owner != null) {
//            owner.setTeam(this);
//        }
//    }
}
