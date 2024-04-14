package com.projeto.pi.projeto_pi.modals.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends PagingAndSortingRepository<User, Long> {
    Optional<User> findById(long id);

    User save(User item);

    void deleteById(Long id);

    Optional<User> findByLoginIgnoreCase(String login);

    @Query(value = "Select * from usuarios where  TO_CHAR(data_nascimento, 'yyyy-mm-dd') = :dtnascimento and upper(login)=upper(:login)", nativeQuery = true)
    Optional<User> findToRecover(@Param("login") String login, @Param("dtnascimento") String dtnascimento);
}
