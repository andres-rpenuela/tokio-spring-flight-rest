package org.tokio.spring.flight.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tokio.spring.flight.api.domain.User;
import org.tokio.spring.flight.api.report.UserReport;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {


    private final UserReport userRepository;

    /**
     * Verifica si el usaaroi logeado es el mismo que el usuario con el "id" dado
     *
     * {@link UserDetailsServiceImpl} para ver el username que se registra en el contexto cuando se hace login
     * @param id del usuario a comprobar
     * @return true si tienen el mismo email, false en caso contrrio
     */
    public boolean hasId(String id){
        org.springframework.security.core.userdetails.User userDetails =  (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
            return true;
        }
        User user = userRepository.findById(id).orElseGet(()->null);
        return Objects.nonNull(user) && Objects.equals(user.getEmail(),userDetails.getUsername());

    }

}