package com.example.fantasy.repository.admin;


import com.example.fantasy.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "player")
public interface PlayerForAdminRepository extends JpaRepository<Player, Long> {

    @Modifying
    @Query("update Player p set p.firstName=:firstName, p.lastName=:lastName, p.country=:country " +
            "where p.id=:id and p.team.owner.id=:ownerId")
    int updatePlayerInformation(@Param("id") Long id, @Param("ownerId") Long ownerId, @Param("firstName") String firstName,
                                @Param("lastName") String lastName, @Param("country") String country);

}
