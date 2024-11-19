package org.tokio.spring.flight.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tokio.spring.flight.api.core.exception.UserException;
import org.tokio.spring.flight.api.dto.UserDTO;
import org.tokio.spring.flight.api.report.UserReport;
import org.tokio.spring.flight.api.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReport userReport;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true) // aseguras que los datos relacionados con carga perezosa pueden cargarse durante la transacción.
    public Optional<Pair<UserDTO, String>> findUserAndPasswordByEmail(String mail) throws UserException {
        final String maybeEmail = Optional.ofNullable(mail)
                .map(StringUtils::stripToNull)
                .orElseThrow(()->new UserException("Email not allow"));

        // devuelve una tupa con el usuarioDTO y la pwd encriptada dentro de un optional
        // o un optional vacio
        return userReport.findByEmail(maybeEmail)
                .map(user ->  Pair.of(modelMapper.map(user, UserDTO.class), user.getPassword()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) throws UserException {
        final String maybeEmail = Optional.ofNullable(email)
                .map(StringUtils::stripToNull)
                .orElseThrow(()->new UserException("Email not allow"));

        return userReport.findByEmail(maybeEmail)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }
}
