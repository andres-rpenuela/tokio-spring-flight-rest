package org.tokio.spring.flight.api.configuration.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.tokio.spring.flight.api.converter.UUIDLocatorToStringLocatorConverter;

import java.util.UUID;

@Configuration
public class FlightBookingDtoMapper {

    private final ModelMapper modelMapper;

    public FlightBookingDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        init();
    }

    private void init() {

        this.modelMapper.typeMap(UUID.class, String.class)
                .setConverter(mappingContext -> new UUIDLocatorToStringLocatorConverter().convert(mappingContext));
    }
}
