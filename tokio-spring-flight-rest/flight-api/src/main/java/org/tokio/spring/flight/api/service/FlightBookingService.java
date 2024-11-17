package org.tokio.spring.flight.api.service;

import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.core.exception.OverBookingException;
import org.tokio.spring.flight.api.dto.FlightBookingDTO;

import java.util.List;
import java.util.Set;

public interface FlightBookingService {

    FlightBookingDTO newBookingFlight(Long flightId, String userId) throws FlightException, OverBookingException;
    Set<FlightBookingDTO> findAllBooking();

    List<FlightBookingDTO> searchBookingsByFlightId(Long flightId) throws FlightException;
}
