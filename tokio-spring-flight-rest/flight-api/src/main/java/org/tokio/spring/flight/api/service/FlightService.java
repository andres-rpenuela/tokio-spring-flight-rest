package org.tokio.spring.flight.api.service;

import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.dto.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlightService {

    List<FlightShowDTO> getAllShowFlights();
    List<FlightMvcDTO> getAllMvcFlights();

    FlightDTO getFlightDTOById(Long flightId) throws FlightException;
    FlightMvcDTO getFlightById(Long flightId) throws FlightException;
    FlightMvcDTO createFlight(FlightMvcDTO flight) throws FlightException;
    FlightMvcDTO createFlight(FlightMvcDTO flight, MultipartFile multipartFile,String description) throws FlightException;
    FlightMvcDTO updated(FlightMvcDTO flightMvcDTO) throws FlightException;
    FlightMvcDTO updated(FlightMvcDTO flightMvcDTO, MultipartFile multipartFile, String description) throws FlightException;

    Optional<FlightMvcDTO> findFlightById(Long idFlight);
    void deleteImage(UUID resourceId) throws FlightException, IllegalArgumentException;
    FlightMvcDTO deleteImageAndGet(Long idFLight,UUID resourceId) throws FlightException;

    PageDTO<FlightShowDTO> searchFlights(FlightSearchRequestDTO flightSearchRequestDTO) throws FlightException;
}
