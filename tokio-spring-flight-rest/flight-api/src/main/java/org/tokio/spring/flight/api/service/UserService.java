package org.tokio.spring.flight.api.service;

import org.modelmapper.internal.Pair;
import org.tokio.spring.flight.api.core.exception.UserException;
import org.tokio.spring.flight.api.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<Pair<UserDTO,String>> findUserAndPasswordByEmail(String mail) throws UserException;
    Optional<UserDTO> findByEmail(String email) throws UserException;
}
