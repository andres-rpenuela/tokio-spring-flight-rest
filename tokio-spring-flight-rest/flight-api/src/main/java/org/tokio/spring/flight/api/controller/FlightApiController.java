package org.tokio.spring.flight.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tokio.spring.flight.api.dto.FlightShowDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightApiController {

    @GetMapping({"/show-flights"})
    public ResponseEntity<List<FlightShowDTO>> listFlightsHandler(){

        FlightShowDTO flightShowDTO =  new FlightShowDTO(1L,"0001","BCN","GLA");
        return ResponseEntity.ok(List.of(flightShowDTO));
    }
}
