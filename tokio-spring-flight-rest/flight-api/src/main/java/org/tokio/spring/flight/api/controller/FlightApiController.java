package org.tokio.spring.flight.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.service.FlightService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightApiController {

    private final FlightService flightService;

    @GetMapping({"/show-flights"})
    public ResponseEntity<List<FlightShowDTO>> listShowFlightsHandler(){

        List<FlightShowDTO> flightShowDTOS = flightService.getAllShowFlights();
        return ResponseEntity.ok(flightShowDTOS);
    }

    @GetMapping({"/list","","/"})
    public ResponseEntity<List<FlightMvcDTO>> listFlightsHandler(){

        List<FlightMvcDTO> flightMvcDTOS = flightService.getAllMvcFlights();
        return ResponseEntity.ok(flightMvcDTOS);
    }
}
