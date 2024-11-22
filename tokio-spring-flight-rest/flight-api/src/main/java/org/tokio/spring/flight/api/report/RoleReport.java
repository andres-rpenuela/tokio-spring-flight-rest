package org.tokio.spring.flight.api.report;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.api.domain.Role;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleReport extends CrudRepository<Role,Long> {

    @Query(value = "select r from Role r where upper(r.name) in (upper(:names)) ")
    Set<Role> getRolesBySetNames(@Param("names") List<String> names);
}
