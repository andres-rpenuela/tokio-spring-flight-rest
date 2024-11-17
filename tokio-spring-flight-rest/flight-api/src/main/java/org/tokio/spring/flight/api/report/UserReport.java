package org.tokio.spring.flight.api.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.api.domain.User;

@Repository
public interface UserReport extends JpaRepository<User,String> {
}
