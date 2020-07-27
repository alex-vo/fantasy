package com.example.fantasy.repository.admin;


import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "player")
public interface PlayerForAdminRepository extends PagingAndSortingRepository<Player, Long> {
}
