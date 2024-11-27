package org.tokio.spring.flight.security;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tokio.spring.flight.dto.UserDTO;
import org.tokio.spring.flight.service.UserService;

import java.util.List;

/**
 * Service para la authentication del usuario con Spring Security
 */
@Service
@RequiredArgsConstructor
public class FlightUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /*
     * Metodo que se encarga de implementar la autenticacion del usuiario
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // autorizacion
        try {
            Pair<UserDTO, String> tupleUserDto = userService.findUserAndPasswordByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("User with suername:%s not found".formatted(username)));

            // se adapta a user details de spring
            return toUserDetails(tupleUserDto.getLeft(),tupleUserDto.getRight());

        }catch (IllegalArgumentException e){
            throw new UsernameNotFoundException("Credenciales erróneas: {}".formatted(username),e);
        }
    }

    private UserDetails toUserDetails(UserDTO userDto,String password) {
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
