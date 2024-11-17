package org.tokio.spring.flight.api.report;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.api.domain.FlightBooking;

import java.util.List;

@Repository
public interface BookingReport extends CrudRepository<FlightBooking, Long> {

    List<FlightBooking> findByFlightId(Long flightId);
    List<FlightBooking> findByUserId(String userId);
}
