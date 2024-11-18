package org.tokio.spring.flight.api.converter;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.tokio.spring.flight.api.domain.Role;

import java.util.List;
import java.util.Set;

public class SetRoleToListRoleDTO implements Converter<Set< Role >, List<String>> {
    @Override
    public List<String> convert(MappingContext<Set < Role >, List<String>> mappingContext) {
        return mappingContext.getSource().stream().map(Role::getName)
                .map(StringUtils::upperCase)
                .toList();
    }
}
