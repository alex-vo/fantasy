package com.example.fantasy.repository.user;

import com.example.fantasy.entity.Team;
import com.example.fantasy.model.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select t " +
            "from Team t " +
            "join fetch t.players p " +
            "where t.owner.id = ?1")
    Optional<TeamModel> findByOwnerId(Long ownerId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Team t set t.name = ?3, t.country = ?4 " +
            "where t.id = (select t.id from Team t where t.id = ?1 and t.owner.id = ?2)")
    int updateTeamInformation(Long id, Long ownerId, String name, String country);

    @Modifying(clearAutomatically = true)
    @Query("update Team t set t.balance = ?2 where t.id = ?1")
    int updateBalance(Long id, BigDecimal sum);

}
