package org.tokio.spring.flight.configuration.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.tokio.spring.flight.configuration.mapper.converter.FligthDTOToFlightMvcDTOConverter;
import org.tokio.spring.flight.dto.FlightDTO;
import org.tokio.spring.flight.dto.FlightMvcDTO;

@Configuration
public class FlightMvcDtoMapperConfiguration {

    private final ModelMapper modelMapper;

    @Autowired
    public FlightMvcDtoMapperConfiguration(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        configure();
    }

    private void configure(){
        modelMapper.typeMap(FlightDTO.class, FlightMvcDTO.class)
                        .setConverter(new FligthDTOToFlightMvcDTOConverter());
    }
}
