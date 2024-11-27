package org.tokio.spring.flight.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tokio.spring.flight.dto.AirportDTO;
import org.tokio.spring.flight.repository.AirportDao;
import org.tokio.spring.flight.service.AirportService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportServiceImp implements AirportService {

    private final AirportDao airportDao;
    private final ModelMapper modelMapper;

    @Override
    public List<AirportDTO> getAllAirports() {
        return airportDao.findAll().stream()
                .map(airport -> modelMapper.map(airport, AirportDTO.class))
                .toList();
    }
}
