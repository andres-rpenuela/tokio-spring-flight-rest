package org.tokio.spring.flight.api.configuration.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.tokio.spring.flight.api.converter.ResourceResourceIdToStringReosurceIdConverter;
import org.tokio.spring.flight.api.domain.Resource;

@Configuration
public class FlightMvcDTOMapper{

    private final ModelMapper modelMapper;

    @Autowired
    public  FlightMvcDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        addConverts();
    }

    private void addConverts(){
        this.modelMapper.typeMap(Resource.class, String.class)
                .setConverter(mappingContext -> new ResourceResourceIdToStringReosurceIdConverter().convert(mappingContext));
    }
}
