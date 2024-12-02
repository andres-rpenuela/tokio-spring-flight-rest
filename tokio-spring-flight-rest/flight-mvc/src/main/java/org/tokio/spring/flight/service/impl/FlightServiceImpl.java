package org.tokio.spring.flight.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.domain.Airport;
import org.tokio.spring.flight.domain.Flight;
import org.tokio.spring.flight.domain.Resource;
import org.tokio.spring.flight.domain.STATUS_FLIGHT;
import org.tokio.spring.flight.dto.FlightDTO;
import org.tokio.spring.flight.dto.FlightMvcDTO;
import org.tokio.spring.flight.dto.ResourceDto;
import org.tokio.spring.flight.repository.AirportDao;
import org.tokio.spring.flight.repository.FlightDao;
import org.tokio.spring.flight.repository.ResourceDao;
import org.tokio.spring.flight.service.FlightService;
import org.tokio.spring.flight.service.ResourceService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightDao flightDao;
    private final AirportDao airportDao;

    @Qualifier("resource-service-mvc")
    private final ResourceService resourceService;
    private final ModelMapper modelMapper;

    private static final List<String> filterFlightDTO = List.of("number", "departureTime", "airportDepartureAcronym", "airportArrivalAcronym", "status");
    private final ResourceDao resourceDao;

    @Override
    public FlightDTO findById(Long id) {
        final Flight flight = findFlightById(id);
        return modelMapper.map(flight, FlightDTO.class);
    }

    @Override
    public String getResourceIdByFlightId(Long id) {
        return Optional.of(findById(id))
                .map(FlightDTO::getFlightImg)
                .map(StringUtils::stripToNull)
                .orElse(null);
    }

    protected Flight findFlightById(Long id){
        return Optional.ofNullable(id)
                .map(flightDao::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElseThrow(() -> new IllegalArgumentException("%d not found!".formatted(id)));
    }

    @Override
    @Transactional
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public FlightDTO createFlight(FlightMvcDTO flightMvcDTO, MultipartFile multipartFile) {
        final Flight flight = createOrEdit(new Flight(),flightMvcDTO,multipartFile);
        return modelMapper.map(flight, FlightDTO.class);
    }


    @Override
    @Transactional
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public FlightDTO editFlight(FlightMvcDTO flightMvcDTO, MultipartFile multipartFile) {
        final Flight flight = createOrEdit(findFlightById(flightMvcDTO.getId()),flightMvcDTO,multipartFile);
        return modelMapper.map(flight, FlightDTO.class);
    }

    // Para que la transacción se propage a los métodos que va invocando, estos deben estar anotados con Transacction
    // o deben ser "protected"
    //@Transactional
    protected Flight createOrEdit(Flight flight,FlightMvcDTO flightMvcDTO, MultipartFile multipartFile) {

        if(!multipartFile.isEmpty()){
            Optional<ResourceDto> resourceDto = null;
            if(flight.getFlightImg() != null){
                resourceDto = resourceService.update(flight.getFlightImg(),multipartFile, "image updated!");
            }else {
                resourceDto = resourceService.save(multipartFile, StringUtils.EMPTY);
            }
            final Resource resource = resourceDto.map(image-> Resource.builder()
                            .id(image.getId())
                            .fileName(image.getFilename())
                            .resourceId(image.getResourceId())
                            .size(image.getSize())
                            .contentType(image.getContentType())
                            .build())
                    .orElse(null);
            flight.setFlightImg(resource);

        }
        flight.setNumber(flightMvcDTO.getNumber());
        flight.setCapacity(flightMvcDTO.getCapacity());
        // si enn el dto es un string, se convierte de String a Temporal con formato
        //flight.setDepartureTime( LocalDateTime.parse(flightMvcDTO.getDayTime(), DateTimeFormatter.ISO_DATE_TIME));
        // si no se deja como esta
        flight.setDepartureTime( flightMvcDTO.getDayTime());

        flight.setStatusFlight(STATUS_FLIGHT.valueOf(flightMvcDTO.getStatus()));
        flight.setAirportArrival(getAirport(flightMvcDTO.getAirportArrivalAcronym()));
        flight.setAirportDeparture(getAirport(flightMvcDTO.getAirportDepartureAcronym()));

        return flightDao.save(flight);
    }

    protected Airport getAirport(String acronym){
        return Optional.ofNullable(acronym)
                .map(airportDao::findByAcronym)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElseThrow(() -> new IllegalArgumentException("%s not found!".formatted(acronym)));
    }

    @Override
    public List<FlightDTO> findAllFlights() {
        return flightDao.findAll().stream()
                .map(flight->modelMapper.map(flight,FlightDTO.class))
                .toList();
    }

    @Override
    public Page<FlightDTO> findAllFlights(int size, int pageNumber) {
        if( size <=0 || pageNumber <0)
            return Page.empty();

        final PageRequest pageRequest = PageRequest.of(pageNumber,size);

        return flightDao.findAll(pageRequest)
                .map(flight->modelMapper.map(flight,FlightDTO.class));
    }

    @Override
    public Page<FlightDTO> findAllFlightsSortField(int size, int pageNumber, String sortField, boolean isSortDirectionAsc) {
        if( size <=0 || pageNumber <0)
            return Page.empty();

        final String orderByProperty = Optional.ofNullable(sortField)
                .map(StringUtils::stripToNull)
                .map(StringUtils::toRootLowerCase)
                .filter(filterFlightDTO::contains)
                .orElse(filterFlightDTO.getFirst());

        final Sort sortCustom = buildSortByFieldAndProperty(orderByProperty, isSortDirectionAsc);
        final PageRequest pageRequest = PageRequest.of(pageNumber,size,sortCustom);

        return flightDao.findAll(pageRequest)
                .map(flight->modelMapper.map(flight,FlightDTO.class));
    }

    @Override
    @Transactional
    public void deleteImage(UUID resourceId) {
         Flight flight = flightDao.findAll()
                 .stream()
                 .filter(flightDTO -> Objects.nonNull(flightDTO.getFlightImg()) &&
                         resourceId.equals(flightDTO.getFlightImg().getResourceId())
                         )
                 .findFirst()
                 .orElseThrow(()->
                         new IllegalArgumentException("flight with resource %s not found!".formatted(resourceId)));
         flight.setFlightImg(null);
         resourceService.deleteImage(resourceId);
    }

    @Override
    public Map<Long, FlightDTO> getFlightsId(HashSet<Long> flightsIds) {
        final List<Flight> flights = flightDao.findAllById(flightsIds);

        return flights.stream()
                .collect(Collectors.toMap(Flight::getId, flight ->  modelMapper.map(flight,FlightDTO.class)) );
    }

    private Sort buildSortByFieldAndProperty(String orderByProperty,boolean isAsc){
        final Sort sortByField = Optional.ofNullable(orderByProperty)
                .map(StringUtils::stripToNull)
                .map(StringUtils::toRootLowerCase)
                .filter(filterFlightDTO::contains)
                .map(Sort::by)
                .orElse(Sort.by(filterFlightDTO.getFirst()));

        return isAsc? sortByField.ascending() : sortByField.descending();
    }
}
