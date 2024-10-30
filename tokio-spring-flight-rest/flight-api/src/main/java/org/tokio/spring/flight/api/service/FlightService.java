package org.tokio.spring.flight.api.service;

import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;

import java.util.List;

public interface FlightService {

    List<FlightShowDTO> getAllShowFlights();
    List<FlightMvcDTO> getAllMvcFlights();
}
