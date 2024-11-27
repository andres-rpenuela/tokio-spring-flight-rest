package org.tokio.spring.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.domain.FlightBooking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlightBookingDao extends JpaRepository<FlightBooking,Long> {

    List<FlightBooking> getFlightBookingByFlightId(Long flightId);
    Optional<FlightBooking> findFLightBookingByLocator(UUID bookingLocator);
    List<FlightBooking> findAll();
}
