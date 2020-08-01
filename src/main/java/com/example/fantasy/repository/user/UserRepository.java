package com.example.fantasy.repository.user;

import com.example.fantasy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    @Query("select u.passwordHash from User u where u.email = :email and u.blocked = false")
    Optional<String> findPasswordHashByEmail(@Param("email") String email);
}
