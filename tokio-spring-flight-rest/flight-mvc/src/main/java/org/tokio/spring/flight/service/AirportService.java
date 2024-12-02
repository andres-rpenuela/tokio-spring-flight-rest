package org.tokio.spring.flight.service;


import org.tokio.spring.flight.dto.AirportDTO;

import java.util.List;

public interface AirportService {

    List<AirportDTO> getAllAirports();
}
