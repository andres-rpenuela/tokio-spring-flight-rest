package org.tokio.spring.flight.api.converter;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.tokio.spring.flight.api.domain.Resource;

import java.util.Optional;
import java.util.UUID;


public class ResourceResourceIdToStringReosurceIdConverter implements Converter<Resource, String> {

    @Override
    public String convert(MappingContext<Resource, String> mappingContext) {
        return Optional.ofNullable(mappingContext)
                .map(MappingContext::getSource)
                .map(Resource::getResourceId)
                .map(UUID::toString)
                .orElse(null);
    }
}
