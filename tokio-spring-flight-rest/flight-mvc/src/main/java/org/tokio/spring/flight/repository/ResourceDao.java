package org.tokio.spring.flight.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.domain.Resource;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceDao extends CrudRepository<Resource, Long> {

    Optional<Resource> findByResourceId(UUID uuid);

}
