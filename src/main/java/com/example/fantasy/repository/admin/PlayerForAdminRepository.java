package com.example.fantasy.repository.admin;


import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;

@RepositoryRestResource(path = "player")
public interface PlayerForAdminRepository extends JpaRepository<Player, Long> {

    @Modifying
    @Query("update Player p set p.firstName=:firstName, p.lastName=:lastName, p.country=:country " +
            "where p.id=:id and p.team.owner.id=:ownerId")
    int updatePlayerInformation(@Param("id") Long id, @Param("ownerId") Long ownerId, @Param("firstName") String firstName,
                                @Param("lastName") String lastName, @Param("country") String country);

    @Modifying
    @Transactional
    @Query("update Player p set p.isOnTransfer = true, p.transferPrice = :price " +
            "where p.id = :id and p.team = (select t from Team t where t.owner.id = :ownerId)")
    int placePlayerOnTransfer(@Param("id") Long id, @Param("ownerId") Long ownerId, @Param("price") BigDecimal price);

    @Query("from Player p where p.id=:id and p.isOnTransfer=true")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Player> findPlayerOnTransferById(@Param("id") Long id);

    @Modifying
    @Query("update Player p set p.team=:team, p.isOnTransfer=false, p.transferPrice=null where p.id=:id")
    int addPlayerToTeam(@Param("id") Long id, @Param("team") Team team);

}
