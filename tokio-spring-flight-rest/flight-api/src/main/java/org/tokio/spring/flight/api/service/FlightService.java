package org.tokio.spring.flight.api.service;

import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;

import java.util.List;

public interface FlightService {

    List<FlightShowDTO> getAllShowFlights();
    List<FlightMvcDTO> getAllMvcFlights();
    FlightMvcDTO getFlightById(Long flightId) throws FlightException;
    FlightMvcDTO createFlight(FlightMvcDTO flight) throws FlightException;
    FlightMvcDTO createFlight(FlightMvcDTO flight, MultipartFile multipartFile,String description) throws FlightException;
    FlightMvcDTO updated(FlightMvcDTO flightMvcDTO) throws FlightException;
    FlightMvcDTO updated(FlightMvcDTO flightMvcDTO, MultipartFile multipartFile, String description) throws FlightException;
}
