package org.tokio.spring.flight.service;
import org.apache.commons.lang3.tuple.Pair;
import org.tokio.spring.flight.dto.UserDTO;

import java.util.Optional;

public interface UserService {

    Optional<Pair<UserDTO,String>> findUserAndPasswordByEmail(String mail);
    Optional<UserDTO> findByEmail(String email);
}
