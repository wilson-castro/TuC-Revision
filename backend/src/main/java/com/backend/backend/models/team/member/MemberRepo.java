package com.backend.backend.models.team.member;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface MemberRepo extends PagingAndSortingRepository<Member, Long> {
    Optional<Member> findById(long id);

    Member save(Member item);

    void deleteById(Long id);

    @Query(value = "Select m.* from membros m where team_id=:teamId", nativeQuery = true)
    Page<Member> getAllMembers(Pageable pageable, @Param("teamId") Long teamId);

    @Query(value = "Select m.* from membros m where team_id=:teamId and user_id=:userId", nativeQuery = true)
    Optional<Member> getTeamMember(@Param("teamId") Long teamId, @Param("userId") Long userId);
}
