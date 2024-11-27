package org.tokio.spring.flight.configuration.mapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.tokio.spring.flight.domain.Role;

import java.util.List;
import java.util.Set;

public class RoleSetToStringListConverter implements Converter<Set<Role>, List<String>> {


    @Override
    public List<String> convert(MappingContext<Set<Role>, List<String>> context) {
        return context.getSource().stream().map(Role::getName).toList();
    }
}
