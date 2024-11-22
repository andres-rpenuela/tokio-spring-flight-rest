package org.tokio.spring.flight.api.configuration.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.tokio.spring.flight.api.converter.SetRoleToListRoleDTO;
import org.tokio.spring.flight.api.domain.User;
import org.tokio.spring.flight.api.dto.UserDTO;

@Configuration
public class UserDTOMapper {

    private final ModelMapper modelMapper;

    public UserDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        init();
    }

    private void init() {
        this.modelMapper.typeMap(User.class, UserDTO.class)
                .addMappings(mapping -> mapping.using(new SetRoleToListRoleDTO()).map(User::getRoles,UserDTO::setRoles));
    }
}
