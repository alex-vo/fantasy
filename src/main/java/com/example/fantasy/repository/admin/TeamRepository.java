package com.example.fantasy.repository.admin;


import com.example.fantasy.entity.Team;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "team")
public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {
}
