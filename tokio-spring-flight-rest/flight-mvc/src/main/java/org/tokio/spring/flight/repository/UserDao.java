package org.tokio.spring.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.domain.User;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User,String> {
    // query methods
    Optional<User> findByEmail(String email);
}
