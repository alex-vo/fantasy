package com.example.fantasy.repository.admin;


import com.example.fantasy.entity.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.LockModeType;
import java.util.Optional;

@RepositoryRestResource(path = "user")
public interface UserForAdminRepository extends PagingAndSortingRepository<User, Long> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findById(Long aLong);

}
