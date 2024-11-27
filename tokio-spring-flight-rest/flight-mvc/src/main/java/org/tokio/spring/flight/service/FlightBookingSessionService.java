package org.tokio.spring.flight.service;

import org.tokio.spring.flight.dto.FlightBookingDTO;
import org.tokio.spring.flight.dto.FlightDTO;
import org.tokio.spring.flight.dto.session.FlightBookingSessionDTO;

import java.util.Map;

public interface FlightBookingSessionService {

    void addFlightId(Long flightId, FlightBookingSessionDTO flightBookingSessionDTO);
    FlightBookingDTO confirmFlightBookingSession(FlightBookingSessionDTO flightBookingSessionDTO);
    Map<Long, FlightDTO> getFlightsById(FlightBookingSessionDTO flightBookingSessionDTO);
}
