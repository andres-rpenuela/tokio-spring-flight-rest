package org.tokio.spring.flight.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransientPropertyValueException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.domain.Airport;
import org.tokio.spring.flight.api.domain.Flight;
import org.tokio.spring.flight.api.domain.Resource;
import org.tokio.spring.flight.api.domain.STATUS_FLIGHT;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.dto.ResourceDTO;
import org.tokio.spring.flight.api.report.AirportReport;
import org.tokio.spring.flight.api.report.FlightReport;
import org.tokio.spring.flight.api.report.ResourceReport;
import org.tokio.spring.flight.api.service.FlightService;
import org.tokio.spring.flight.api.service.ManagementResourceService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {

    private final FlightReport flightReport;
    private final AirportReport airportReport;

    private final ManagementResourceService managementResourceService;
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
        if(Objects.isNull(flightMvcDTO)) {
            throw new FlightException("flight is null");
        }

       Flight flight = populationCreateOrEditFlight(Flight.builder().build(),flightMvcDTO,null);
       return modelMapper.map(flight, FlightMvcDTO.class);
    }

    @Override
    @Transactional
    public FlightMvcDTO createFlight(FlightMvcDTO flightMvcDTO, MultipartFile multipartFile, String description) throws FlightException {
        if(Objects.isNull(flightMvcDTO)){
            throw new FlightException("Can't be null flight");
        }

        Resource resource = null;
        if(!multipartFile.isEmpty()){

            Optional<ResourceDTO>  resourceDTOOptional =  managementResourceService.save(multipartFile,description);
                resource = resourceDTOOptional.map(resourceDTO -> Resource.builder()
                                .id(resourceDTO.getId())
                                .resourceId(resourceDTO.getResourceId())
                                .size(resourceDTO.getSize())
                                .contentType(resourceDTO.getContentType())
                                .fileName(resourceDTO.getFilename())
                                .build())
                        .orElseGet(() -> null);
        }

        Flight flight = populationCreateOrEditFlight(Flight.builder().build(),flightMvcDTO,resource);
        return modelMapper.map(flight, FlightMvcDTO.class);
    }

    @Override
    @Transactional
    public FlightMvcDTO updated(FlightMvcDTO flightMvcDTO) throws FlightException {
        if(Objects.isNull(flightMvcDTO)){
            throw new FlightException("The flight to updated can't be null");
        }

        Flight flight = flightReport.findById(flightMvcDTO.getId())
                .orElseThrow(()->
                        new FlightException("The flight with id %s not found in hte system.".
                                formatted(flightMvcDTO.getId())));

        final Airport airportDeparture = updatedAirportByAcronym(flight.getAirportDeparture().getAcronym(),flightMvcDTO.getAirportDepartureAcronym())
                .orElseGet(flight::getAirportDeparture);

        final Airport airportArrival = updatedAirportByAcronym(flight.getAirportArrival().getAcronym(),flightMvcDTO.getAirportArrivalAcronym())
                .orElseGet(flight::getAirportArrival);

        // update flight
        flight.setCapacity(flightMvcDTO.getCapacity());
        flight.setNumber(flightMvcDTO.getFlightNumber());
        flight.setDepartureTime(flightMvcDTO.getDepartureTime());
        flight.setAirportArrival(airportArrival);
        flight.setAirportDeparture(airportDeparture);
        flight.setStatusFlight(STATUS_FLIGHT.valueOf(flightMvcDTO.getStatus()));

        // updated or create
        try {
            flightReport.save(flight);
        }catch (DataAccessException e){
            log.error("Can't create flight {0}",e);
            throw new FlightException("Can't create flight, because: %s ".formatted(e.getMessage()),e);
        }
        return modelMapper.map(flight,FlightMvcDTO.class);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected Optional<Airport> updatedAirportByAcronym(@NonNull String sourceAcronym, @NonNull String targetAcronym)
            throws FlightException{
        if (Objects.equals(sourceAcronym, targetAcronym)) {
            return Optional.empty();
        }

        final Airport maybeAirport = airportReport
                .findByAcronym(targetAcronym)
                .orElseThrow(() ->
                        new FlightException("The Airport with Acronym %s not found.".formatted(targetAcronym)));

        return Optional.of(maybeAirport);
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

    private Flight populationCreateOrEditFlight(Flight flight, FlightMvcDTO flightMvcDTO,Resource resource) throws FlightException,DataAccessException{
        Airport airportArrival = airportReport.findByAcronym(flightMvcDTO.getAirportArrivalAcronym())
                .orElseThrow(()->new FlightException("The acronym airport %s don't found".formatted(flightMvcDTO.getAirportArrivalAcronym())));

        Airport airportDeparture = airportReport.findByAcronym(flightMvcDTO.getAirportDepartureAcronym())
                .orElseThrow(()->new FlightException("The acronym airport %s don't found".formatted(flightMvcDTO.getAirportDepartureAcronym())));

        flight.setAirportArrival(airportArrival);
        flight.setAirportDeparture(airportDeparture);
        flight.setCapacity(flightMvcDTO.getCapacity());
        flight.setNumber(flightMvcDTO.getFlightNumber());
        flight.setDepartureTime(flightMvcDTO.getDepartureTime());
        flight.setStatusFlight(STATUS_FLIGHT.valueOf(flightMvcDTO.getStatus()));

        if(Objects.nonNull(resource)) {
            flight.setFlightImg(resource);
        }

        try {
            flightReport.save(flight);
        }catch (TransientPropertyValueException | DataAccessException e){
            if( Objects.nonNull( flight.getFlightImg() )) { // deleted
                managementResourceService.deleteImage(flight.getFlightImg().getResourceId());
            }
            log.error("Can't create flight {0}",e);
            throw new FlightException("Can't create flight, because: %s ".formatted(e.getMessage()),e);
        }

        return flight;
    }
}
