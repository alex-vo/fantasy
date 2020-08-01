package com.example.fantasy.repository.admin;


import com.example.fantasy.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;


@PreAuthorize("hasRole('ROLE_ADMIN')")
@RepositoryRestResource(path = "user")
public interface SecuredUserRepository extends PagingAndSortingRepository<User, Long> {

}
