package org.tokio.spring.flight.configuration.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.tokio.spring.flight.configuration.mapper.converter.RoleSetToStringListConverter;
import org.tokio.spring.flight.domain.User;
import org.tokio.spring.flight.dto.UserDTO;

@Configuration
public class UserDtoMapper {
    // sobrecarga del mode mapper
    private final ModelMapper modelMapper;

    public UserDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        configureUserDTOConverter();
    }

    private void configureUserDTOConverter(){
        modelMapper.typeMap(User.class, UserDTO.class)
                .addMappings(mapping ->
                        mapping.using(new RoleSetToStringListConverter()).map(User::getRoles,UserDTO::setRoles)
                );
    }
}
