package org.tokio.spring.flight.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tokio.spring.flight.api.domain.Flight;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.report.FlightReport;
import org.tokio.spring.flight.api.service.FlightService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {

    private final FlightReport flightReport;
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
