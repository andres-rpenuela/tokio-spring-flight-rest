package org.tokio.spring.flight.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.api.core.exception.UserException;
import org.tokio.spring.flight.api.domain.Resource;
import org.tokio.spring.flight.api.domain.Role;
import org.tokio.spring.flight.api.domain.User;
import org.tokio.spring.flight.api.dto.ResourceDTO;
import org.tokio.spring.flight.api.dto.UserDTO;
import org.tokio.spring.flight.api.dto.UserFormDTO;
import org.tokio.spring.flight.api.report.RoleReport;
import org.tokio.spring.flight.api.report.UserReport;
import org.tokio.spring.flight.api.service.ManagementResourceService;
import org.tokio.spring.flight.api.service.UserService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReport userReport;
    private final RoleReport roleReport;

    private final ManagementResourceService managementResourceService;

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

    @Override
    @Transactional
    public UserFormDTO created(@NonNull UserFormDTO userFormDTO) throws UserException {
        final String maybeEmail = StringUtils.stripToNull(userFormDTO.getEmail());
        if ( userReport.findByEmail(maybeEmail).isPresent() ) {
             throw new UserException("Email already in use");
        }

        // return collection empty, if the parma is null or not found in bbdd
        Set<Role> roles = getRoles(userFormDTO);

        User user = new User();
        fillUserFromUserFormDTO(user,userFormDTO,roles);
        user = userReport.save(user);

        return modelMapper.map(user, UserFormDTO.class);
    }

    @Override
    @Transactional
    public UserFormDTO created(UserFormDTO userFormDTO, MultipartFile multipartFile, String description) throws UserException {
        final String maybeEmail = StringUtils.stripToNull(userFormDTO.getEmail());
        if ( userReport.findByEmail(maybeEmail).isPresent() ) {
            throw new UserException("Email already in use");
        }

        // return collection empty, if the parma is null or not found in bbdd
        Set<Role> roles = getRoles(userFormDTO);

        // magnament imag user
        Resource resource = null;
        if(multipartFile != null && !multipartFile.isEmpty()) {
            Optional<ResourceDTO> imag = managementResourceService.save(multipartFile,description);
            resource = imag.map(resourceDTO -> Resource.builder()
                    .id(resourceDTO.getId())
                    .size(resourceDTO.getSize())
                    .fileName(resourceDTO.getFilename())
                    .resourceId(resourceDTO.getResourceId())
                    .contentType(resourceDTO.getContentType())
                    .build()).orElseGet(()->null);
        }

        User user = new User();
        fillUserFromUserFormDTO(user,userFormDTO,roles,resource);
        user = userReport.save(user);

        return modelMapper.map(user, UserFormDTO.class);
    }

    @Override
    @Transactional
    public UserFormDTO updated(String userId, UserFormDTO userFormDTO) throws UserException {
        User user = userReport.findById(userId).orElseThrow(()->new UserException("User not found"));
        final String maybeEmail = StringUtils.stripToNull(userFormDTO.getEmail());
        if(!Objects.equals(user.getEmail(),maybeEmail) && userReport.findByEmail(maybeEmail).isPresent()){
            throw new UserException("Email already in use");
        }


        Set<Role> roles = getRoles(userFormDTO);
        fillUserFromUserFormDTO(user,userFormDTO,roles);
        user = userReport.save(user);

        return modelMapper.map(user, UserFormDTO.class);
    }

    @Override
    public UserFormDTO updated(String userId, UserFormDTO userFormDTO, MultipartFile multipartFile, String description) throws UserException {
        User user = userReport.findById(userId).orElseThrow(()->new UserException("User not found"));
        final String maybeEmail = StringUtils.stripToNull(userFormDTO.getEmail());
        if(!Objects.equals(user.getEmail(),maybeEmail) && userReport.findByEmail(maybeEmail).isPresent()){
            throw new UserException("Email already in use");
        }

        // return collection empty, if the parma is null or not found in bbdd
        Set<Role> roles = getRoles(userFormDTO);

        // magnament imag user
        final UUID userImgOld = Optional.ofNullable(user.getUserImage()).map(Resource::getResourceId).orElseGet(()->null);
        Resource resource = null;
        if(multipartFile != null && !multipartFile.isEmpty()) {
            Optional<ResourceDTO> imag = managementResourceService.save(multipartFile,description);
            resource = imag.map(resourceDTO -> Resource.builder()
                    .id(resourceDTO.getId())
                    .size(resourceDTO.getSize())
                    .fileName(resourceDTO.getFilename())
                    .resourceId(resourceDTO.getResourceId())
                    .contentType(resourceDTO.getContentType())
                    .build()).orElseGet(()->null);
            if(resource != null && !Objects.equals(resource.getResourceId(),userImgOld)) {
                managementResourceService.deleteImage(userImgOld);
            }
        }

        fillUserFromUserFormDTO(user,userFormDTO,roles,resource);
        user = userReport.save(user);

        return modelMapper.map(user, UserFormDTO.class);
    }

    private Set<Role> getRoles(UserFormDTO userFormDTO) {
        // return collection empty, if the parma is null or not found in bbdd
        Set<Role> roles = roleReport.getRolesBySetNames(userFormDTO.getRoles());
        if( roles.isEmpty() ) {
            throw new UserException("Roles cannot be empty");
        }
        return roles;
    }

    private static void fillUserFromUserFormDTO(@NonNull User user, @NonNull UserFormDTO userFormDTO, @NonNull Set<Role> roles){
        user.setId(userFormDTO.getId());
        user.setName(userFormDTO.getName());
        user.setCreated(LocalDateTime.now());
        user.setEmail(userFormDTO.getEmail());
        user.setSurname(userFormDTO.getSurname());
        user.setPassword(userFormDTO.getPassword());
        user.setActive(Boolean.TRUE);
        user.setRoles(roles);
    }

    private static void fillUserFromUserFormDTO(@NonNull User user, @NonNull UserFormDTO userFormDTO, @NonNull Set<Role> roles, @Nullable Resource resource){
        fillUserFromUserFormDTO(user,userFormDTO,roles);
        user.setUserImage(resource);
        user.setRoles(roles);
    }
}
