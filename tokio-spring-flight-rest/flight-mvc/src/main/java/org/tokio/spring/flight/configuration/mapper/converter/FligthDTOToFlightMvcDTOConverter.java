package org.tokio.spring.flight.configuration.mapper.converter;



import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.tokio.spring.flight.dto.FlightDTO;
import org.tokio.spring.flight.dto.FlightMvcDTO;

import java.util.Optional;

public class FligthDTOToFlightMvcDTOConverter implements Converter<FlightDTO, FlightMvcDTO> {


    @Override
    public org.tokio.spring.flight.dto.FlightMvcDTO convert(MappingContext<FlightDTO, FlightMvcDTO> mappingContext) {
        return Optional.ofNullable(mappingContext)
                .map(MappingContext::getSource)
                .map(FligthDTOToFlightMvcDTOConverter::buildFlightDto)
                .orElse(null);
    }

    private static FlightMvcDTO buildFlightDto(FlightDTO flightDto) {
        return FlightMvcDTO.builder()
                .number(flightDto.getNumber())
                .capacity(flightDto.getCapacity())
                .status(flightDto.getStatus().name())
                //.dayTime(flightDto.getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME)) // Temp., a String
                .dayTime(flightDto.getDepartureTime())
                .airportArrivalAcronym(flightDto.getAirportArrivalAcronym())
                .airportDepartureAcronym(flightDto.getAirportDepartureAcronym())
                .id(flightDto.getId())
                .build();
    }

}
