package org.tokio.spring.flight.api.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tokio.spring.flight.api.domain.Flight;

import java.util.Optional;

public interface FlightReport extends JpaRepository<Flight,Long> {

}
