package org.tokio.spring.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String name;
    private String surname;
    private String email;
    private LocalDate lastLogin;
    private List<String> roles;
}
