package com.example.fantasy.repository.admin;


import com.example.fantasy.entity.Team;
import com.example.fantasy.model.TeamModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.Optional;

@RepositoryRestResource(path = "team")
public interface SecuredTeamRepository extends PagingAndSortingRepository<Team, Long> {

    @Query("select t " +
            "from Team t " +
            "join fetch t.players p " +
            "where t.owner.id = :ownerId")
    Optional<TeamModel> findByOwnerId(@Param("ownerId") Long ownerId);

    @Modifying
    @Query("update Team t set t.name=:name, t.country=:country " +
            "where t.id=:id and t.owner.id=:ownerId")
    int updateTeamInformation(@Param("id") Long id, @Param("ownerId") Long ownerId, @Param("name") String name,
                              @Param("country") String country);

    @Modifying
    @Query("update Team t set t.balance = t.balance + :sum where t.id = :id")
    int topUpBalance(@Param("id") Long id, @Param("sum") BigDecimal sum);

    //TODO maybe make idempotent?
    @Modifying
    @Query("update Team t set t.balance = t.balance - :sum where t.id = :id")
    int reduceBalance(@Param("id") Long id, @Param("sum") BigDecimal sum);

}
