package org.tokio.spring.flight.api.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.UUID;

public class UUIDLocatorToStringLocatorConverter implements Converter<UUID, String> {


    @Override
    public String convert(MappingContext<UUID, String> mappingContext) {
        return mappingContext.getSource().toString();
    }
}
