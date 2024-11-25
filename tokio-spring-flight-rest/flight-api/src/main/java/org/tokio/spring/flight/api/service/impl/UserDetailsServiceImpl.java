package org.tokio.spring.flight.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Pair;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tokio.spring.flight.api.dto.UserDTO;
import org.tokio.spring.flight.api.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    /*
     * Metodo que se encarga de implementar la autenticacion del usuiario
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // autorizacion
        try {
            Pair<UserDTO, String> tupleUserDto = userService.findUserAndPasswordByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("User with email:%s not found".formatted(username)));

            // se adapta a user details de spring
            return toUserDetails(tupleUserDto.getLeft(), tupleUserDto.getRight());

        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Credenciales erróneas: {}".formatted(username), e);
        }
    }

    private UserDetails toUserDetails(UserDTO userDto, String password) {
        // Se crea la lista de autoridades a partir de los roles,
        // se puede considader un rol una autoridad, pero esto puede estar separado
        final List<SimpleGrantedAuthority> simpleGrantedAuthorities = userDto.getRoles().stream()
                .map(SimpleGrantedAuthority::new).toList();

        return new org.springframework.security.core.userdetails.User(
                userDto.getEmail(), // identidad
                password, // credenciales (encriptada)
                simpleGrantedAuthorities// autoridades
        );
    }
}