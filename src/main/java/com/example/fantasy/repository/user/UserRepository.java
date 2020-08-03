package com.example.fantasy.repository.user;

import com.example.fantasy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u " +
            "join fetch u.team t " +
            "where u.id = ?1")
    Optional<User> findById(Long id);

    @Query("select u from User u where u.email = ?1 and u.blocked = false")
    Optional<User> findNonBlockedByEmail(String email);

    @Modifying
    @Transactional
    @Query("update User u " +
            "set u.failedLoginAttempts = ?2, " +
            "  u.blocked = case " +
            "    when ?2 > 2 then true " +
            "    else false " +
            "  end " +
            "where u.id = ?1")
    void updateFailedLoginAttemptsCount(Long id, Integer failedLoginAttempts);
}
