package org.tokio.spring.flight.api.controller;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tokio.spring.flight.api.dto.UserDTO;
import org.tokio.spring.flight.api.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/user-login")
    public ResponseEntity<Pair<UserDTO,String>> getUserLoginHandlerByEmail( @Email @RequestParam("email") String email) {
        Optional<Pair<UserDTO,String>> userAndPasswordOpt = userService.findUserAndPasswordByEmail(email);
        return userAndPasswordOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
