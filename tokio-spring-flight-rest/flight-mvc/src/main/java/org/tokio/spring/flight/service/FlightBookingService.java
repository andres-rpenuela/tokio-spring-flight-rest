package org.tokio.spring.flight.service;


import org.tokio.spring.flight.dto.FlightBookingDTO;
import org.tokio.spring.flight.exception.OverBookingException;

import java.util.Set;

public interface FlightBookingService {

    FlightBookingDTO newBookingFlight(Long flightId, String userId) throws IllegalArgumentException, OverBookingException;

    Set<FlightBookingDTO> findAllBooking();
}
