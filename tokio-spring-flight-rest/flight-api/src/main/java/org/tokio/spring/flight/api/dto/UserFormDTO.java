package org.tokio.spring.flight.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.tokio.spring.flight.api.core.constans.ROLES;
import org.tokio.spring.flight.api.core.validation.annotation.status.RoleValid;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@Jacksonized
public class UserFormDTO {

    private String id;
    @NotEmpty
    private String name;
    private String surname;
    @NotEmpty
    private String password;
    @NotEmpty @Email
    private String email;
    @RoleValid(target = ROLES.class)
    private List<String> roles;
}
