package org.tokio.spring.flight.api.service;

import jakarta.validation.Valid;
import org.modelmapper.internal.Pair;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.api.core.exception.UserException;
import org.tokio.spring.flight.api.dto.UserDTO;
import org.tokio.spring.flight.api.dto.UserFormDTO;

import java.util.Optional;

public interface UserService {
    Optional<Pair<UserDTO,String>> findUserAndPasswordByEmail(String mail) throws UserException;
    Optional<UserDTO> findByEmail(String email) throws UserException;

    UserFormDTO created(@NonNull UserFormDTO userFormDTO) throws UserException;
    UserFormDTO created(@NonNull UserFormDTO userFormDTO, MultipartFile multipartFile, @Nullable String description) throws UserException;
    UserFormDTO updated(@NonNull String userId, @NonNull UserFormDTO userFormDTO) throws UserException;
    UserFormDTO updated(@NonNull String userId, @NonNull UserFormDTO userFormDTO, MultipartFile multipartFile, @Nullable String description) throws UserException;

}
