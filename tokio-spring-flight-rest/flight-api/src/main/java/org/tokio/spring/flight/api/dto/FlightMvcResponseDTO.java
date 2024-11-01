package org.tokio.spring.flight.api.dto;


import java.util.Map;

public record FlightMvcResponseDTO(Map<String,String> errors, FlightMvcDTO flightMvcDTO){
}
