package org.tokio.spring.flight.configuration.mapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.tokio.spring.flight.domain.Resource;

import java.util.Optional;

public class StringToResourceConvert implements Converter<String, Resource> {


    @Override
    public Resource convert(MappingContext<String,Resource> mappingContext) {
        return Optional.ofNullable(mappingContext)
                .map(MappingContext::getSource)
                .map(stringId -> Resource.builder().id(Long.parseLong(stringId)).build())
                .orElse(null);
    }

}
