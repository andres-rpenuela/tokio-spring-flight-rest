package org.tokio.spring.flight.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.domain.Role;

@Repository
public interface RoleDao extends CrudRepository<Role,Long> {

    // method query
    Role findByNameIgnoreCase(String name);

}
