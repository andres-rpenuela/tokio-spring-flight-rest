package org.tokio.spring.flight.configuration.mapper.converter;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.tokio.spring.flight.domain.Resource;

import java.util.Optional;

/**
 * Crea un conversor personalizado de Resoruce a String
 */
public class ResourceToStringConvert implements Converter<Resource,String> {


    @Override
    public String convert(MappingContext<Resource, String> mappingContext) {
        return Optional.ofNullable(mappingContext)
                .map(MappingContext::getSource)
                .map(Resource::getResourceId)
                .map(String::valueOf)
                .orElse(null);
    }

}
