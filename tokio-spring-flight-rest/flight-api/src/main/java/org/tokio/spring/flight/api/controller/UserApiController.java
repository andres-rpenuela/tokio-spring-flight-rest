package org.tokio.spring.flight.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.tokio.spring.flight.api.core.exception.UserException;
import org.tokio.spring.flight.api.dto.ResponseDTO;
import org.tokio.spring.flight.api.dto.UserDTO;
import org.tokio.spring.flight.api.dto.UserFormDTO;
import org.tokio.spring.flight.api.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/user-login")
    public ResponseEntity<Pair<UserDTO,String>> getUserLoginHandlerByEmailHandler( @Email @RequestParam("email") String email) {
        Optional<Pair<UserDTO,String>> userAndPasswordOpt = userService.findUserAndPasswordByEmail(email);
        return userAndPasswordOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user-by-email")
    public ResponseEntity<UserDTO> getUserByEmailHandler(@Email @RequestParam("email") String email){
        UserDTO userDTO = userService.findByEmail(email).orElseThrow(() -> new UserException("User not found! "));
        return Objects.nonNull(userDTO) ? ResponseEntity.ok(userDTO) : ResponseEntity.notFound().build();
    }

    @PostMapping("/created")
    public ResponseEntity<ResponseDTO<UserFormDTO>> addUserHandler(@Valid @RequestBody UserFormDTO userFormDTO, BindingResult bindingResult){
        HttpStatus status = HttpStatus.OK;
        List<ObjectError> errors = null;

        if(bindingResult.hasErrors()){
            status = HttpStatus.BAD_REQUEST;
            errors = bindingResult.getAllErrors();
        }
        // TODO Create
        userFormDTO = userService.created(userFormDTO);
        return ResponseEntity.status(status).body(new ResponseDTO<>(errors, userFormDTO));
    }

    @PutMapping("/updated/{id}")
    public ResponseEntity<ResponseDTO<UserFormDTO>> addUserHandler(@PathVariable(name="id") String userId, @Valid @RequestBody UserFormDTO userFormDTO, BindingResult bindingResult){
        HttpStatus status = HttpStatus.OK;
        List<ObjectError> errors = null;

        if(bindingResult.hasErrors()){
            status = HttpStatus.BAD_REQUEST;
            errors = bindingResult.getAllErrors();
        }
        // TODO Created
        return ResponseEntity.status(status).body(new ResponseDTO<>(errors, userFormDTO));
    }
}
