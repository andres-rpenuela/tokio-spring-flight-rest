package org.tokio.spring.flight.api.report;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.api.domain.FlightBooking;

@Repository
public interface BookingReport extends CrudRepository<FlightBooking, Long> {
}
