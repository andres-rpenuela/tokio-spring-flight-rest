package org.tokio.spring.flight.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.domain.Airport;
import org.tokio.spring.flight.api.domain.Flight;
import org.tokio.spring.flight.api.domain.STATUS_FLIGHT;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.report.AirportReport;
import org.tokio.spring.flight.api.report.FlightReport;
import org.tokio.spring.flight.api.service.FlightService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {

    private final FlightReport flightReport;
    private final AirportReport airportReport;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FlightShowDTO> getAllShowFlights() {
        List<Flight> flights = flightReport.findAll();

        return flights.stream()
                .map(FlightServiceImpl::mapFlightToFlightShowDTO)
                .toList();
    }

    @Override
    public List<FlightMvcDTO> getAllMvcFlights() {
        List<Flight> flights = flightReport.findAll();

        return flights.stream()
                .map(flight -> modelMapper.map(flight, FlightMvcDTO.class))
                .toList();
    }

    @Override
    public FlightMvcDTO getFlightById(Long flightId) throws FlightException {
        if(Objects.isNull(flightId)) {
            throw new FlightException("flight id is null");
        }
        Flight flight = flightReport.findById(flightId)
                .orElseThrow(()-> new FlightException("Flight with id: %d not found!".formatted(flightId)));

        return modelMapper.map(flight, FlightMvcDTO.class);
    }

    @Override
    @Transactional
    public FlightMvcDTO createFlight(FlightMvcDTO flightMvcDTO) throws FlightException {
        // TODO
        if(Objects.isNull(flightMvcDTO)) {
            throw new FlightException("flight is null");
        }

        Airport airportArrival = airportReport.findByAcronym(flightMvcDTO.getAirportArrivalAcronym())
                .orElseThrow(()->new FlightException("The acronym airport %s don't found".formatted(flightMvcDTO.getAirportArrivalAcronym())));

        Airport airportDeparture = airportReport.findByAcronym(flightMvcDTO.getAirportDepartureAcronym())
                .orElseThrow(()->new FlightException("The acronym airport %s don't found".formatted(flightMvcDTO.getAirportDepartureAcronym())));

        Flight flight = Flight.builder()
                .airportArrival(airportArrival)
                .airportDeparture(airportDeparture)
                .capacity(flightMvcDTO.getCapacity())
                .number(flightMvcDTO.getFlightNumber())
                .departureTime(flightMvcDTO.getDepartureTime())
                .statusFlight(STATUS_FLIGHT.valueOf(flightMvcDTO.getStatus()))
                .build();

        try {
            flightReport.save(flight);
        }catch (DataAccessException e){
            log.error("Can't create flight {0}",e);
            throw new FlightException("Can't create flight, because: %s ".formatted(e.getMessage()),e);
        }
        return modelMapper.map(flight, FlightMvcDTO.class);
    }


    protected static FlightShowDTO mapFlightToFlightShowDTO(@NonNull Flight flight) {
        try {
            return FlightShowDTO.builder()
                    .id(flight.getId())
                    .arrival(flight.getAirportArrival().getAcronym())
                    .departure(flight.getAirportDeparture().getAcronym())
                    .number(flight.getNumber())
                    .build();
        }catch (NullPointerException e){
            log.error("Error to map Flight to FlightShowDTO ",e);
            return null;
        }
    }
}
