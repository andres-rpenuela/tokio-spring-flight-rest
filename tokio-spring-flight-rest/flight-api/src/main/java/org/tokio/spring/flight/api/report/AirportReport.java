package org.tokio.spring.flight.api.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tokio.spring.flight.api.domain.Airport;

import java.util.Optional;

public interface AirportReport extends JpaRepository<Airport,Long> {

    Optional<Airport> findByAcronym(String acronym);
}
