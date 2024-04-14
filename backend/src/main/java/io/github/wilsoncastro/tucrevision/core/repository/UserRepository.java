package io.github.wilsoncastro.tucrevision.core.repository;

import io.github.wilsoncastro.tucrevision.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmailEqualsIgnoreCase(String email);

    boolean existsByEmailEqualsIgnoreCaseAndIdIsNot(String email, Long id);

}
