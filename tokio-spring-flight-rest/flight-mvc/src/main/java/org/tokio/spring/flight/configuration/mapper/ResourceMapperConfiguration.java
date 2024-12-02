package org.tokio.spring.flight.configuration.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.tokio.spring.flight.configuration.mapper.converter.ResourceToStringConvert;
import org.tokio.spring.flight.configuration.mapper.converter.StringToResourceConvert;
import org.tokio.spring.flight.domain.Resource;

@Configuration
public class ResourceMapperConfiguration {

    private final ModelMapper modelMapper;

    @Autowired
    public ResourceMapperConfiguration(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        configure();
    }

    private void configure(){
        modelMapper.typeMap(Resource.class, String.class)
                .setConverter(mappingContext -> new ResourceToStringConvert().convert(mappingContext));
        modelMapper.typeMap(String.class,Resource.class)
                .setConverter(mappingContext -> new StringToResourceConvert().convert(mappingContext));
    }
}
