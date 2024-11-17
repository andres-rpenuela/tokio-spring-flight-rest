package org.tokio.spring.flight.api.service;

import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.core.exception.OverBookingException;
import org.tokio.spring.flight.api.dto.FlightBookingDTO;

import java.util.Set;
import java.util.UUID;

public interface FlightBookingService {

    FlightBookingDTO newBookingFlight(Long flightId, String userId) throws FlightException, OverBookingException;
    Set<FlightBookingDTO> findAllBooking();
}
