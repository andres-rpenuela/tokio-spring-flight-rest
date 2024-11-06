package org.tokio.spring.flight.api.report;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.api.domain.Resource;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceReport extends CrudRepository<Resource,Long> {
    Optional<Resource> findByResourceId(UUID uuid);
}
