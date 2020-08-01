package com.example.fantasy.repository.admin;


import com.example.fantasy.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "player")
public interface SecuredPlayerRepository extends JpaRepository<Player, Long> {
}
