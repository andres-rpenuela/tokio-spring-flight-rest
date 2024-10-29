package org.tokio.spring.flight.api.dto;

import lombok.Builder;

@Builder
public record FlightShowDTO(Long id,String number,String arrival,String departure) {
}
