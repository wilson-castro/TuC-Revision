package com.backend.backend.models.team;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface TeamRepo extends PagingAndSortingRepository<Team, Long> {
    Optional<Team> findById(long id);

    Team save(Team item);

    void deleteById(Long id);

}
