package org.tokio.spring.flight.service;


import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.dto.FlightDTO;
import org.tokio.spring.flight.dto.FlightMvcDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FlightService {

    FlightDTO findById(Long id);
    String getResourceIdByFlightId(Long id);

    //@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    FlightDTO createFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile);

    //@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    FlightDTO editFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile);

    List<FlightDTO> findAllFlights();
    Page<FlightDTO> findAllFlights(int size,int pageNumber);
    Page<FlightDTO> findAllFlightsSortField(int size,int pageNumber,String sortField,boolean isSortDirectionAsc);

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    void deleteImage(UUID resourceId);

    Map<Long, FlightDTO> getFlightsId(HashSet<Long> flightsIds);
}
