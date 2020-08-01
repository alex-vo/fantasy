package com.example.fantasy.repository.user;

import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.Team;
import com.example.fantasy.model.PlayerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("select p from Player p where p.id = ?1")
    Optional<PlayerModel> findPlayerById(Long id);

    @Query("select p from Player p " +
            "join p.team t " +
            "where p.isOnTransfer = true " +
            "  and (?1 is null or p.country = ?1) " +
            "  and (?2 is null or t.name = ?2) " +
            "  and (?3 is null or p.lastName = ?3) " +
            "  and (?4 is null or p.value >= ?4) " +
            "  and (?5 is null or p.value <= ?5) " +
            "order by p.lastName")
    Page<PlayerModel> findPlayersOnTransfer(String country, String teamName, String playerLastName,
                                            BigDecimal valueMin, BigDecimal valueMax, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Player p set p.firstName = ?3, p.lastName = ?4, p.country = ?5 " +
            "where p.id in (select p.id from Player p where p.id = ?1 and p.team.owner.id = ?2)")
    int updatePlayerInformation(Long id, Long ownerId, String firstName, String lastName, String country);

    @Modifying
    @Transactional
    @Query("update Player p set p.isOnTransfer = true, p.transferPrice = ?3 " +
            "where p.id = ?1 and p.team in (select t from Team t where t.owner.id = ?2)")
    int placePlayerOnTransfer(Long id, Long ownerId, BigDecimal price);

    @Query("from Player p where p.id = ?1 and p.isOnTransfer = true")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Player> findPlayerOnTransferById(Long id);

    @Modifying
    @Query("update Player p set p.team = ?2, p.isOnTransfer = false, p.transferPrice = null, p.value = ?3 where p.id = ?1")
    void performTransfer(Long id, Team team, BigDecimal newValue);

}
